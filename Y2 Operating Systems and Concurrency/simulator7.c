#include "coursework.h"
#include "linkedlist.h"
#include <stdio.h>
#include <pthread.h>
#include <stdlib.h>
#include <sys/time.h>
#include <semaphore.h>

sem_t empty, sync, full, tDsem, pDsem;
pthread_mutex_t mutex;
LinkedList oReadyQueue = LINKED_LIST_INITIALIZER;
LinkedList oTerminatedQueue = LINKED_LIST_INITIALIZER;
LinkedList oPageFaultQueue = LINKED_LIST_INITIALIZER;
LinkedList oFrameList = LINKED_LIST_INITIALIZER;

int i, j, oldest = 0;
int pageFcount = 0;
bool done = false;
bool sim = false;

LinkedList hash_table[SIZE_OF_PROCESS_TABLE];



void * processGenerator(void *arg)
{
	while(1)
	{
	Process *pd;

	sem_wait(&empty);
	sem_wait(&sync);


	if(done)
	{
		sem_post(&sync);
		return NULL;
	}
	
	if(!sim)
	{
		sem_post(&full);
		sim = true;
	}

	if(i < MAX_CONCURRENT_PROCESSES)
	{
		pd = generateProcess(j);
		if(pd->iPID != -1)
		{	
			pthread_mutex_lock(&mutex);
			addLast(pd, &oReadyQueue);
			printf("ADMITTED: [PID = %d, BurstTime = %d, RemainingBurstTime = %d, Locality = %d, Width = %d]\n",pd->iPID, pd->iBurstTime, pd->iRemainingBurstTime, pd->iLocality, pd->iWidth);

			addFirst(pd, &(hash_table[pd->iHash]));
	
			i++;
			j++;
			pthread_mutex_unlock(&mutex);
			
		}
			
	}
	
	
	sem_post(&sync);
	
	}
}


void * processSimulator(void * arg)
{
	
	while(1)
	{
	Process *tempProcess;
	Element *current;
	Process *hash;


	sem_wait(&full);
	

	pthread_mutex_lock(&mutex);
	
	if(getHead(oReadyQueue))
	{
		tempProcess = (getHead(oReadyQueue)->pData);

		current = getHead(hash_table[tempProcess->iHash]);
		hash = current->pData;

		while(hash->iPID != tempProcess->iPID)
		{
			current = getNext(current);
			hash = current->pData;
		}
		printf("SIMULATING: [PID = %d, BurstTime = %d, RemainingBurstTime = %d]\n", hash->iPID, hash->iBurstTime, hash->iRemainingBurstTime);
	}
	pthread_mutex_unlock(&mutex);


	sem_wait(&sync);

	
	tempProcess = removeFirst(&oReadyQueue);
	runPreemptiveProcess(tempProcess, true);
		
		
	if(tempProcess->iStatus == READY)
	{
		addLast(tempProcess, &oReadyQueue);
		printf("READY: [PID = %d, BurstTime = %d, RemainingBurstTime = %d, Locality = %d, Width = %d]\n",tempProcess->iPID, tempProcess->iBurstTime, tempProcess->iRemainingBurstTime, tempProcess->iLocality, tempProcess->iWidth);	
		sem_post(&full);	
	}
	else if(tempProcess->iStatus == TERMINATED)
	{
		addLast(tempProcess, &oTerminatedQueue);	
		sem_post(&tDsem);
		i--;	
		sim = false;	
	}
	else if(tempProcess->iStatus == PAGE_FAULTED)
	{
		addLast(tempProcess, &oPageFaultQueue);	
		sem_post(&pDsem);	
		pageFcount++;
	}
	
	if(j == NUMBER_OF_PROCESSES && !(getHead(oReadyQueue)) && !(getHead(oPageFaultQueue)) )
	{
		sem_post(&sync);
		return NULL;
	}
	sem_post(&sync);
	}
}


void * terminationDaemon(void * arg)
{
	while(1)
	{
	
	Process *tempProcessTdae;
	Process *hash;
	long int difference = 0;
	long int difference2 = 0;
	
	sem_wait(&tDsem);
	sem_wait(&sync);


	tempProcessTdae = removeFirst(&oTerminatedQueue);
	printf("TERMINATED: [PID = %d, RemainingBurstTime = %li]\n", tempProcessTdae->iPID, tempProcessTdae->iRemainingBurstTime);	
	

	hash = removeData(tempProcessTdae, &(hash_table[tempProcessTdae->iHash]));
	difference = getDifferenceInMilliSeconds(hash->oTimeCreated, hash->oFirstTimeRunning);
	difference2 = getDifferenceInMilliSeconds(hash->oTimeCreated, hash->oLastTimeRunning);
	printf("CLEARED: [PID = %d, ResponseTime = %li, TurnAroundTime = %li, PageFaults = % d]\n", hash->iPID, difference, difference2, hash->iPageFaults);
	
		
	free(tempProcessTdae);

	if( j == NUMBER_OF_PROCESSES && !(getHead(oReadyQueue)) && !(getHead(oTerminatedQueue)) && !(getHead(oPageFaultQueue)) )
	{
		done = true;
		sem_post(&sync);
		sem_post(&empty);
		sem_post(&pDsem);
		printf("SIMULATION FINISHED: Total PageFaults: %d, Average PageFaults: %f\n", pageFcount, (double)pageFcount/NUMBER_OF_PROCESSES);
		return NULL;
	}

	if(j<NUMBER_OF_PROCESSES)
	{
		sem_post(&empty);
	}

	else if(j == NUMBER_OF_PROCESSES)
		sem_post(&full);

	sem_post(&sync);
	

	}
}


void * pagingDaemon(void * arg)
{
	while(1)
	{

	FrameEntry * pframe;
	Element *current;
	
	sem_wait(&pDsem);
	sem_wait(&sync);

	if(done)
	{
		sem_post(&sync);
		return NULL;
	}

	Process *tempProcessPdae = removeFirst(&oPageFaultQueue);

	printf("PAGE FAULTED: [PID = %d, BurstTime = %d, RemainingBurstTime = %d, Locality = %d, Width = %d, Page = %d, Offset = %d]\n",tempProcessPdae->iPID, tempProcessPdae->iBurstTime, tempProcessPdae->iRemainingBurstTime, tempProcessPdae->iLocality, tempProcessPdae->iWidth, tempProcessPdae->oLastRequested.iPage, tempProcessPdae->oLastRequested.iOffset);	

	if(PAGING == FIFO_PAGING)
	{

		current = getHead(oFrameList);
	
		for(int a = 0; a<NUMBER_OF_FRAMES; a++)
		{
			pframe = current->pData;
	
			if(pframe->iFrame == oldest)
			{
				reclaimFrame(pframe);
				mapFrame(tempProcessPdae, pframe);	
				oldest++;
				break;
			}
			else
			{
				current = getNext(current);
			}	
				
		}
		
		tempProcessPdae->iStatus = READY;
		addLast(tempProcessPdae, &oReadyQueue);
		sem_post(&full);
	
		if(oldest == NUMBER_OF_FRAMES)
		{
			oldest = 0;
		}

	}

	else if(PAGING == NRU_PAGING)
	{
		bool found = false;
		while(!found)
		{
			current = getHead(oFrameList);
			for(int b = 0; b<NUMBER_OF_FRAMES; b++)
			{
				pframe = current->pData;
	
				if(pframe->iAccessed == 0 && pframe->iDirty == 0)
				{
					reclaimFrame(pframe);
					mapFrame(tempProcessPdae, pframe);	
					found = true;
					break;
				}
				else
				{
					current = getNext(current);
				}	
				
			}
			if(!found)
			{
				current = getHead(oFrameList);
				for(int b = 0; b<NUMBER_OF_FRAMES; b++)
				{
					pframe = current->pData;
	
					if(pframe->iAccessed == 0 && pframe->iDirty == 1)
					{
						reclaimFrame(pframe);
						mapFrame(tempProcessPdae, pframe);	
						found = true;
						break;
					}
					else
					{
						pframe->iAccessed = 0;
						current = getNext(current);
					}	
				
				}
			}
		}

		
		tempProcessPdae->iStatus = READY;
		addLast(tempProcessPdae, &oReadyQueue);
		sem_post(&full);


	}

	sem_post(&sync);
	

	}
}



int main(int argc, char **argv)
{
	Element *e;
	

	pthread_t pGen, pSim, tDae, pDae;

	for(int a = 0; a<(int)NUMBER_OF_FRAMES; a++)
	{
		FrameEntry * pframe = malloc(sizeof(FrameEntry));
		*pframe = (FrameEntry) PAGE_TABLE_ENTRY_INITIALIZER;
		pframe->iFrame = a;
		addLast(pframe, &oFrameList); 	
	}


	sem_init(&empty, 0, MAX_CONCURRENT_PROCESSES);
	sem_init(&sync, 0, 1);
	sem_init(&full, 0, 0);
	sem_init(&tDsem, 0, 0);
	sem_init(&pDsem, 0, 0);
	pthread_mutex_init(&mutex, NULL);

	pthread_create(&tDae, NULL, &terminationDaemon, NULL);
	pthread_create(&pDae, NULL, &pagingDaemon, NULL);
	pthread_create(&pGen, NULL, &processGenerator, NULL);
	pthread_create(&pSim, NULL, &processSimulator, NULL);

	pthread_join(tDae, NULL);
	pthread_join(pGen, NULL);
	pthread_join(pSim, NULL);
	pthread_join(pDae, NULL);

	pthread_mutex_destroy(&mutex);
	sem_destroy(&empty);
	sem_destroy(&sync);
	sem_destroy(&full);
	sem_destroy(&tDsem);
	sem_destroy(&pDsem);

	for(int a = 0; a<(int)NUMBER_OF_FRAMES; a++)
	{
		FrameEntry * pframe = removeFirst(&oFrameList);
		free(pframe);
	}		

	return 0;
}


