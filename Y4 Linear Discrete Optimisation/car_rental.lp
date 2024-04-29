/*
* CAR RENTAL 2
* MIP Problem
* INDICES
* i, j = Glasgow, Manchester, Birmingham, Plymouth : (1, 2, 3, 4)
* t = Monday, Tuesday, Wednesday, Thursday, Friday, Saturday : (1, 2, 3, 4, 5, 6)
* k = 1, 2, 3 (days hired)

* EXCEL CELL REFERENCES FOR SHEET "Car Rental 2"
*/

/* Objective function: Maximise Weekly Profit via a steady state solution of cars at each depot*/
max:
    //1. Profit of cars rented Monday-Friday and returned to the same depot
    //SUM[i=Glasgow to Plymouth] SUM[t=Monday to Friday] SUM[k=1 to 3]
    //        P_ii Q_k (RCA_k - CS_k + 10) tr_it
    //Cells BJ12:BM26
    +34.8tr11 +34.8tr12 +34.8tr13 +34.8tr14 +34.8tr15  //13.20+6.60+15.00 = 34.8 for each term
    +31.9tr21 +31.9tr22 +31.9tr23 +31.9tr24 +31.9tr25  //12.10+6.05+13.75 = 31.9 for each term
    +31.32tr31+31.32tr32+31.32tr33+31.32tr34+31.32tr35 //11.88+5.94+13.50 = 31.32 for each term
    +30.74tr41+30.74tr42+30.74tr43+30.74tr44+30.74tr45 //11.66+5.83+13.25 = 30.74 for each term


    //2. Profit of cars rented Monday-Friday and returned to a different depot
    //SUM[i=Glasgow to Plymouth] SUM[j=Glasgow to Plymouth] SUM[t=Monday to Friday] SUM[k=1 to 3]
    //        P_ij Q_k (RCB_k - CS_k + 10) tr_it        where i!=j
    //Cells BJ47:BM106
    +33tr11     +33tr12     +33tr13     +33tr14     +33tr15      //6.60+3.40+6.500+3.30+1.70+3.250+3.30+1.70+3.250 = 33 for each term
    +37.125tr21 +37.125tr22 +37.125tr23 +37.125tr24 +37.125tr25  //4.95+2.55+4.875+8.25+4.25+8.125+1.65+0.85+1.625 = 37.125 for each term
    +37.95tr31  +37.95tr32  +37.95tr33  +37.95tr34  +37.95tr35   //4.95+2.55+4.875+6.60+3.40+6.500+3.63+1.87+3.575 = 37.95 for each term
    +38.775tr41 +38.775tr42 +38.775tr43 +38.775tr44 +38.775tr45  //2.64+1.36+2.600+3.96+2.04+3.900+8.91+4.59+8.775 = 38.775 for each term


    ///tr_i6 in following Notations is used where t=Saturday///

    //3. Profit of cars rented Saturday and returned to the same depot using the 1-day hire discount
    //SUM[i=Glasgow to Plymouth]
    //        P_ii Q_1 (RCC - CS_1 + 10) tr_i6
    //Cells CQ12:CT12
    +6.6tr16  +6.05tr26 +5.94tr36 +5.83tr46
    

    //4. Profit of cars rented Saturday and returned to a different depot using the 1-day hire discount
    //SUM[i=Glasgow to Plymouth] SUM[j=Glasgow to Plymouth]
    //        P_ij Q_1 (RCD - CS_1 + 10) tr_i6        where i!=j
    //Cells CQ31:CT34
    +4.4tr16  +2.2tr16  +2.2tr16
    +3.3tr26  +5.5tr26  +1.1tr26
    +3.3tr36  +4.4tr36  +2.42tr36
    +1.76tr46 +2.46tr46 +5.94tr46
    

    //5. Profit of cars rented Saturday and returned to the same depot without discount, so 2-day and 3-day hire
    //SUM[i=Glasgow to Plymouth] SUM[k=2 to 3]
    //        P_ii Q_k (RCA_k - CS_k + 10) tr_i6
    //Cells CQ55:CT57
    +6.6tr16  +15tr16
    +6.05tr26 +13.75tr26
    +5.94tr36 +13.5tr36
    +5.83tr46 +13.25tr46
    

    //6. Profit of cars rented Saturday and returned to a different depot without discount, so 2-day and 3-day hire
    //SUM[i=Glasgow to Plymouth] SUM[j=Glasgow to Plymouth] SUM[k=2 to 3]
    //        P_ij Q_k (RCB_k - CS_k + 10) tr_i6      where i!=j
    //Cells CQ78:CT89
    +3.4tr16  +1.7tr16  +1.7tr16  +6.5tr16  +3.25tr16 +3.25tr16
    +2.55tr26 +4.25tr26 +0.85tr26 +4.875tr26+8.125tr26+1.625tr26
    +2.55tr36 +3.4tr36  +1.87tr36 +4.875tr36+6.5tr36  +3.575tr36
    +1.36tr46 +2.04tr46 +4.59tr46 +2.6tr46  +3.9tr46  +8.775tr46
    

    //7. Cost of Undamaged transfers
    //SUM[i=Glasgow to Plymouth] SUM[j=Glasgow to Plymouth] SUM[t=Monday to Saturday]
    //        C_ij tu_ijt
    //Cells DU14:DX37
    -999tu111 -999tu112 -999tu113 -999tu114 -999tu115 -999tu116
    -20tu121  -20tu122  -20tu123  -20tu124  -20tu125  -20tu126
    -30tu131  -30tu132  -30tu133  -30tu134  -30tu135  -30tu136
    -50tu141  -50tu142  -50tu143  -50tu144  -50tu145  -50tu146

    -20tu211  -20tu212  -20tu213  -20tu214  -20tu215  -20tu216
    -999tu221 -20tu222  -999tu223 -999tu224 -999tu225 -999tu226
    -15tu231  -15tu232  -15tu233  -15tu234  -15tu235  -15tu236
    -35tu241  -35tu242  -35tu243  -35tu244  -35tu245  -35tu246

    -30tu311  -30tu312  -30tu313  -30tu314  -30tu315  -30tu316
    -15tu321  -15tu322  -15tu323  -15tu324  -15tu325  -15tu326
    -999tu331 -999tu332 -999tu333 -999tu334 -999tu335 -999tu336
    -25tu341  -25tu342  -25tu343  -25tu344  -25tu345  -25tu346

    -50tu411  -50tu412  -50tu413  -50tu414  -50tu415  -50tu416
    -35tu421  -35tu422  -35tu423  -35tu424  -35tu425  -35tu426
    -25tu431  -25tu432  -25tu433  -25tu434  -25tu435  -25tu436
    -999tu441 -999tu442 -999tu443 -999tu444 -999tu445 -999tu446
    

    //8. Cost of Damaged transfers
    //SUM[i=Glasgow to Plymouth] SUM[j=Glasgow to Plymouth] SUM[t=Monday to Saturday]
    //        C_ij td_ijt
    //Cells DU49:DX72
    -999td111 -999td112 -999td113 -999td114 -999td115 -999td116
    -20td121  -20td122  -20td123  -20td124  -20td125  -20td126
    -30td131  -30td132  -30td133  -30td134  -30td135  -30td136
    -50td141  -50td142  -50td143  -50td144  -50td145  -50td146
    
    -20td211  -20td212  -20td213  -20td214  -20td215  -20td216
    -999td221 -20td222  -999td223 -999td224 -999td225 -999td226
    -15td231  -15td232  -15td233  -15td234  -15td235  -15td236
    -35td241  -35td242  -35td243  -35td244  -35td245  -35td246
    
    -30td311  -30td312  -30td313  -30td314  -30td315  -30td316
    -15td321  -15td322  -15td323  -15td324  -15td325  -15td326
    -999td331 -999td332 -999td333 -999td334 -999td335 -999td336
    -25td341  -25td342  -25td343  -25td344  -25td345  -25td346
    
    -50td411  -50td412  -50td413  -50td414  -50td415  -50td416
    -35td421  -35td422  -35td423  -35td424  -35td425  -35td426
    -25td431  -25td432  -25td433  -25td434  -25td435  -25td436
    -999td441 -999td442 -999td443 -999td444 -999td445 -999td446
    

    //9. Opportunity Cost
    //15*n
    //Cells DV83
    -15n

    //Car Rental 2//
    
    //10. Costs for expansions
    //-(18000B1 + 8000B2 + 20000M1 + 5000M2 + 19000P)
    //Cells DV97:DV101
    -18000b1 - 8000b2 - 20000m1 - 5000m2 - 19000p;


/******************************** Constraints ********************************/

/* Variables for constraints 1 and 2, extracted for efficency
   SUM[j=Glasgow to Plymouth] SUM[k=1 to 3]
                 P_ji Q_k tr_jt-k
                      for i=Glasgow to Plymouth, t=Monday to Saturday
   Cells AT140:AW211
*/
sumtr_jtk11 =
0.33tr16   + 0.12tr15   + 0.15tr14   + 0.0825tr26 + 0.03tr25  + 0.0375tr24 +
0.0825tr36 + 0.03tr35   + 0.0375tr34 + 0.044tr46  + 0.016tr45 + 0.02tr44;
sumtr_jtk21 =
0.11tr16   + 0.04tr15   + 0.05tr14   + 0.3025tr26 + 0.11tr25  + 0.1375tr24 +
0.11tr36   + 0.04tr35   + 0.05tr34   + 0.066tr46  + 0.024tr45 + 0.03tr44;
sumtr_jtk31 =
0.055tr16   + 0.02tr15  + 0.025tr14  + 0.1375tr26 + 0.05tr25  + 0.0625tr24 +
0.297tr36   + 0.108tr35 + 0.135tr34  + 0.1485tr46 + 0.054tr45 + 0.0675tr44;
sumtr_jtk41 =
0.055tr16   + 0.02tr15  + 0.025tr14  + 0.0275tr26 + 0.01tr25  + 0.0125tr24 +
0.0605tr36  + 0.022tr35 + 0.0275tr34 + 0.2915tr46 + 0.106tr45 + 0.1325tr44;

sumtr_jtk12 =
0.33tr11   + 0.12tr16   + 0.15tr15   + 0.0825tr21 + 0.03tr26  + 0.0375tr25 +
0.0825tr31 + 0.03tr36   + 0.0375tr35 + 0.044tr41  + 0.016tr46 + 0.02tr45;
sumtr_jtk22 =
0.11tr11   + 0.04tr16   + 0.05tr15   + 0.3025tr21 + 0.11tr26  + 0.1375tr25 +
0.11tr31   + 0.04tr36   + 0.05tr35   + 0.066tr41  + 0.024tr46 + 0.03tr45;
sumtr_jtk32 =
0.055tr11   + 0.02tr16  + 0.025tr15  + 0.1375tr21 + 0.05tr26  + 0.0625tr25 +
0.297tr31   + 0.108tr36 + 0.135tr35  + 0.1485tr41 + 0.054tr46 + 0.0675tr45;
sumtr_jtk42 =
0.055tr11   + 0.02tr16  + 0.025tr15  + 0.0275tr21 + 0.01tr26  + 0.0125tr25 +
0.0605tr31  + 0.022tr36 + 0.0275tr35 + 0.2915tr41 + 0.106tr46 + 0.1325tr45;

sumtr_jtk13 =
0.33tr12   + 0.12tr11   + 0.15tr16   + 0.0825tr22 + 0.03tr21  + 0.0375tr26 +
0.0825tr32 + 0.03tr31   + 0.0375tr36 + 0.044tr42  + 0.016tr41 + 0.02tr46;
sumtr_jtk23 =
0.11tr12   + 0.04tr11   + 0.05tr16   + 0.3025tr22 + 0.11tr21  + 0.1375tr26 +
0.11tr32   + 0.04tr31   + 0.05tr36   + 0.066tr42  + 0.024tr41 + 0.03tr46;
sumtr_jtk33 =
0.055tr12   + 0.02tr11  + 0.025tr16  + 0.1375tr22 + 0.05tr21  + 0.0625tr26 +
0.297tr32   + 0.108tr31 + 0.135tr36  + 0.1485tr42 + 0.054tr41 + 0.0675tr46;
sumtr_jtk43 =
0.055tr12   + 0.02tr11  + 0.025tr16  + 0.0275tr22 + 0.01tr21  + 0.0125tr26 +
0.0605tr32  + 0.022tr31 + 0.0275tr36 + 0.2915tr42 + 0.106tr41 + 0.1325tr46;

sumtr_jtk14 =
0.33tr13   + 0.12tr12   + 0.15tr11   + 0.0825tr23 + 0.03tr22  + 0.0375tr21 +
0.0825tr33 + 0.03tr32   + 0.0375tr31 + 0.044tr43  + 0.016tr42 + 0.02tr41;
sumtr_jtk24 =
0.11tr13   + 0.04tr12   + 0.05tr11   + 0.3025tr23 + 0.11tr22  + 0.1375tr21 +
0.11tr33   + 0.04tr32   + 0.05tr31   + 0.066tr43  + 0.024tr42 + 0.03tr41;
sumtr_jtk34 =
0.055tr13   + 0.02tr12  + 0.025tr11  + 0.1375tr23 + 0.05tr22  + 0.0625tr21 +
0.297tr33   + 0.108tr32 + 0.135tr31  + 0.1485tr43 + 0.054tr42 + 0.0675tr41;
sumtr_jtk44 =
0.055tr13   + 0.02tr12  + 0.025tr11  + 0.0275tr23 + 0.01tr22  + 0.0125tr21 +
0.0605tr33  + 0.022tr32 + 0.0275tr31 + 0.2915tr43 + 0.106tr42 + 0.1325tr41;

sumtr_jtk15 =
0.33tr14   + 0.12tr13   + 0.15tr12   + 0.0825tr24 + 0.03tr23  + 0.0375tr22 +
0.0825tr34 + 0.03tr33   + 0.0375tr32 + 0.044tr44  + 0.016tr43 + 0.02tr42;
sumtr_jtk25 =
0.11tr14   + 0.04tr13   + 0.05tr12   + 0.3025tr24 + 0.11tr23  + 0.1375tr22 +
0.11tr34   + 0.04tr33   + 0.05tr32   + 0.066tr44  + 0.024tr43 + 0.03tr42;
sumtr_jtk35 =
0.055tr14   + 0.02tr13  + 0.025tr12  + 0.1375tr24 + 0.05tr23  + 0.0625tr22 +
0.297tr34   + 0.108tr33 + 0.135tr32  + 0.1485tr44 + 0.054tr43 + 0.0675tr42;
sumtr_jtk45 =
0.055tr14   + 0.02tr13  + 0.025tr12  + 0.0275tr24 + 0.01tr23  + 0.0125tr22 +
0.0605tr34  + 0.022tr33 + 0.0275tr32 + 0.2915tr44 + 0.106tr43 + 0.1325tr42;

sumtr_jtk16 =
0.33tr15   + 0.12tr14   + 0.15tr13   + 0.0825tr25 + 0.03tr24  + 0.0375tr23 +
0.0825tr35 + 0.03tr34   + 0.0375tr33 + 0.044tr45  + 0.016tr44 + 0.02tr43;
sumtr_jtk26 =
0.11tr15   + 0.04tr14   + 0.05tr13   + 0.3025tr25 + 0.11tr24  + 0.1375tr23 +
0.11tr35   + 0.04tr34   + 0.05tr33   + 0.066tr45  + 0.024tr44 + 0.03tr43;
sumtr_jtk36 =
0.055tr15   + 0.02tr14  + 0.025tr13  + 0.1375tr25 + 0.05tr24  + 0.0625tr23 +
0.297tr35   + 0.108tr34 + 0.135tr33  + 0.1485tr45 + 0.054tr44 + 0.0675tr43;
sumtr_jtk46 =
0.055tr15   + 0.02tr14  + 0.025tr13  + 0.0275tr25 + 0.01tr24  + 0.0125tr23 +
0.0605tr35  + 0.022tr34 + 0.0275tr33 + 0.2915tr45 + 0.106tr44 + 0.1325tr43;


/* CONSTRAINT 1
*  Total number of undamaged cars into depot i on day t
*  0.9 SUM[j=Glasgow to Plymouth] SUM[k=1 to 3] P_ji Q_k tr_jt-k + SUM[j=Glasgow to Plymouth] tu_jit-1 + rp_it-1 + eu_it-1 = nu_it
*      for i=Glasgow to Plymouth, t=Monday to Saturday
*  Cells AB10:AE15
*/
0.9sumtr_jtk11 + tu116 + tu216 + tu316 + tu416 + rp16 + eu16 = nu11;
0.9sumtr_jtk12 + tu111 + tu211 + tu311 + tu411 + rp11 + eu11 = nu12;
0.9sumtr_jtk13 + tu112 + tu212 + tu312 + tu412 + rp12 + eu12 = nu13;
0.9sumtr_jtk14 + tu113 + tu213 + tu313 + tu413 + rp13 + eu13 = nu14;
0.9sumtr_jtk15 + tu114 + tu214 + tu314 + tu414 + rp14 + eu14 = nu15;
0.9sumtr_jtk16 + tu115 + tu215 + tu315 + tu415 + rp15 + eu15 = nu16;

0.9sumtr_jtk21 + tu126 + tu226 + tu326 + tu426 + rp26 + eu26 = nu21;
0.9sumtr_jtk22 + tu121 + tu221 + tu321 + tu421 + rp21 + eu21 = nu22;
0.9sumtr_jtk23 + tu122 + tu222 + tu322 + tu422 + rp22 + eu22 = nu23;
0.9sumtr_jtk24 + tu123 + tu223 + tu323 + tu423 + rp23 + eu23 = nu24;
0.9sumtr_jtk25 + tu124 + tu224 + tu324 + tu424 + rp24 + eu24 = nu25;
0.9sumtr_jtk26 + tu125 + tu225 + tu325 + tu425 + rp25 + eu25 = nu26;

0.9sumtr_jtk31 + tu136 + tu236 + tu331 + tu436 + rp36 + eu36 = nu31;
0.9sumtr_jtk32 + tu131 + tu231 + tu331 + tu431 + rp31 + eu31 = nu32;
0.9sumtr_jtk33 + tu132 + tu232 + tu332 + tu432 + rp32 + eu32 = nu33;
0.9sumtr_jtk34 + tu133 + tu233 + tu333 + tu433 + rp33 + eu33 = nu34;
0.9sumtr_jtk35 + tu134 + tu234 + tu334 + tu434 + rp34 + eu34 = nu35;
0.9sumtr_jtk36 + tu135 + tu235 + tu335 + tu435 + rp35 + eu35 = nu36;

0.9sumtr_jtk41 + tu146 + tu246 + tu346 + tu446 + rp46 + eu46 = nu41;
0.9sumtr_jtk42 + tu141 + tu241 + tu341 + tu441 + rp41 + eu41 = nu42;
0.9sumtr_jtk43 + tu142 + tu242 + tu342 + tu442 + rp42 + eu42 = nu43;
0.9sumtr_jtk44 + tu143 + tu243 + tu343 + tu443 + rp43 + eu43 = nu44;
0.9sumtr_jtk45 + tu144 + tu244 + tu344 + tu444 + rp44 + eu44 = nu45;
0.9sumtr_jtk46 + tu145 + tu245 + tu345 + tu445 + rp45 + eu45 = nu46;

/* CONSTRAINT 2
*  Total number of damaged cars into depot i on day t
*  0.1 SUM[j=Glasgow to Plymouth] SUM[k=1 to 3] P_ji Q_k tr_jt-k + SUM[j=Glasgow to Plymouth] td_jit-1 + ed_it-1 = nd_it
*      for i=Glasgow to Plymouth, t=Monday to Saturday
*  Cells AB23:AE28
*/
0.1sumtr_jtk11 + td116 + td216 + td316 + td416 + ed16 = nd11;
0.1sumtr_jtk12 + td111 + td211 + td311 + td411 + ed11 = nd12;
0.1sumtr_jtk13 + td112 + td212 + td312 + td412 + ed12 = nd13;
0.1sumtr_jtk14 + td113 + td213 + td313 + td413 + ed13 = nd14;
0.1sumtr_jtk15 + td114 + td214 + td314 + td414 + ed14 = nd15;
0.1sumtr_jtk16 + td115 + td215 + td315 + td415 + ed15 = nd16;

0.1sumtr_jtk21 + td126 + td226 + td326 + td426 + ed26 = nd21;
0.1sumtr_jtk22 + td121 + td221 + td321 + td421 + ed21 = nd22;
0.1sumtr_jtk23 + td122 + td222 + td322 + td422 + ed22 = nd23;
0.1sumtr_jtk24 + td123 + td223 + td323 + td423 + ed23 = nd24;
0.1sumtr_jtk25 + td124 + td224 + td324 + td424 + ed24 = nd25;
0.1sumtr_jtk26 + td125 + td225 + td325 + td425 + ed25 = nd26;

0.1sumtr_jtk31 + td136 + td236 + td331 + td436 + ed36 = nd31;
0.1sumtr_jtk32 + td131 + td231 + td331 + td431 + ed31 = nd32;
0.1sumtr_jtk33 + td132 + td232 + td332 + td432 + ed32 = nd33;
0.1sumtr_jtk34 + td133 + td233 + td333 + td433 + ed33 = nd34;
0.1sumtr_jtk35 + td134 + td234 + td334 + td434 + ed34 = nd35;
0.1sumtr_jtk36 + td135 + td235 + td335 + td435 + ed35 = nd36;

0.1sumtr_jtk41 + td146 + td246 + td346 + td446 + ed46 = nd41;
0.1sumtr_jtk42 + td141 + td241 + td341 + td441 + ed41 = nd42;
0.1sumtr_jtk43 + td142 + td242 + td342 + td442 + ed42 = nd43;
0.1sumtr_jtk44 + td143 + td243 + td343 + td443 + ed43 = nd44;
0.1sumtr_jtk45 + td144 + td244 + td344 + td444 + ed44 = nd45;
0.1sumtr_jtk46 + td145 + td245 + td345 + td445 + ed45 = nd46;


/* CONSTRAINT 3
*  Total number of undamaged cars out of depot i on day t
*  tr_it + SUM[j=Glasgow to Plymouth] tu_ijt + eu_it = nu_it
*        for i=Glasgow to Plymouth, t=Monday to Saturday
*  Cells AB35:AE40
*/
tr11 + tu111 + tu121 + tu131 + tu141 + eu11 = nu11;
tr12 + tu112 + tu122 + tu132 + tu142 + eu12 = nu12;
tr13 + tu113 + tu123 + tu133 + tu143 + eu13 = nu13;
tr14 + tu114 + tu124 + tu134 + tu144 + eu14 = nu14;
tr15 + tu115 + tu125 + tu135 + tu145 + eu15 = nu15;
tr16 + tu116 + tu126 + tu136 + tu146 + eu16 = nu16;

tr21 + tu211 + tu221 + tu231 + tu241 + eu21 = nu21;
tr22 + tu212 + tu222 + tu232 + tu242 + eu22 = nu22;
tr23 + tu213 + tu223 + tu233 + tu243 + eu23 = nu23;
tr24 + tu214 + tu224 + tu234 + tu244 + eu24 = nu24;
tr25 + tu215 + tu225 + tu235 + tu245 + eu25 = nu25;
tr26 + tu216 + tu226 + tu236 + tu246 + eu26 = nu26;

tr31 + tu311 + tu321 + tu331 + tu341 + eu31 = nu31;
tr32 + tu312 + tu322 + tu332 + tu342 + eu32 = nu32;
tr33 + tu313 + tu323 + tu333 + tu343 + eu33 = nu33;
tr34 + tu314 + tu324 + tu334 + tu344 + eu34 = nu34;
tr35 + tu315 + tu325 + tu335 + tu345 + eu35 = nu35;
tr36 + tu316 + tu326 + tu336 + tu346 + eu36 = nu36;

tr41 + tu411 + tu421 + tu431 + tu441 + eu41 = nu41;
tr42 + tu412 + tu422 + tu432 + tu442 + eu42 = nu42;
tr43 + tu413 + tu423 + tu433 + tu443 + eu43 = nu43;
tr44 + tu414 + tu424 + tu434 + tu444 + eu44 = nu44;
tr45 + tu415 + tu425 + tu435 + tu445 + eu45 = nu45;
tr46 + tu416 + tu426 + tu436 + tu446 + eu46 = nu46;

/* CONSTRAINT 4
*  Total number of damaged cars out of depot i on day t
*  rp_it-1 + SUM[j = Glasgow to Plymouth] td_ijt + ed_it = nd_it
*          for i=Glasgow to Plymouth, t=Monday to Saturday
*  Cells AB47:AE52
*/
rp16 + td111 + td121 + td131 + td141 + ed11 = nd11;
rp11 + td112 + td122 + td132 + td142 + ed12 = nd12;
rp12 + td113 + td123 + td133 + td143 + ed13 = nd13;
rp13 + td114 + td124 + td134 + td144 + ed14 = nd14;
rp14 + td115 + td125 + td135 + td145 + ed15 = nd15;
rp15 + td116 + td126 + td136 + td146 + ed16 = nd16;

rp26 + td211 + td221 + td231 + td241 + ed21 = nd21;
rp21 + td212 + td222 + td232 + td242 + ed22 = nd22;
rp22 + td213 + td223 + td233 + td243 + ed23 = nd23;
rp23 + td214 + td224 + td234 + td244 + ed24 = nd24;
rp24 + td215 + td225 + td235 + td245 + ed25 = nd25;
rp25 + td216 + td226 + td236 + td246 + ed26 = nd26;

rp36 + td311 + td321 + td331 + td341 + ed31 = nd31;
rp31 + td312 + td322 + td332 + td342 + ed32 = nd32;
rp32 + td313 + td323 + td333 + td343 + ed33 = nd33;
rp33 + td314 + td324 + td334 + td344 + ed34 = nd34;
rp34 + td315 + td325 + td335 + td345 + ed35 = nd35;
rp35 + td316 + td326 + td336 + td346 + ed36 = nd36;

rp46 + td411 + td421 + td431 + td441 + ed41 = nd41;
rp41 + td412 + td422 + td432 + td442 + ed42 = nd42;
rp42 + td413 + td423 + td433 + td443 + ed43 = nd43;
rp43 + td414 + td424 + td434 + td444 + ed44 = nd44;
rp44 + td415 + td425 + td435 + td445 + ed45 = nd45;
rp45 + td416 + td426 + td436 + td446 + ed46 = nd46;

/* CONSTRAINT 5
*  Repair capacity of depot i on all days
*  rp_it <= R_i
*        for i=Glasgow to Plymouth, t=Monday to Saturday
*  Cells AB59:AE64
*/
rp11 <= 0;
rp12 <= 0;
rp13 <= 0;
rp14 <= 0;
rp15 <= 0;
rp16 <= 0;
//Birmingham, Manchester and Plymouth have 5B1 +5B2, 5M1 +5M2 and 5P, respectively added
rp21 <= 12+5m1+5m2;
rp22 <= 12+5m1+5m2;
rp23 <= 12+5m1+5m2;
rp24 <= 12+5m1+5m2;
rp25 <= 12+5m1+5m2;
rp26 <= 12+5m1+5m2;

rp31 <= 20+5b1+5b2;
rp32 <= 20+5b1+5b2;
rp33 <= 20+5b1+5b2;
rp34 <= 20+5b1+5b2;
rp35 <= 20+5b1+5b2;
rp36 <= 20+5b1+5b2;

rp41 <= 0+5p;
rp42 <= 0+5p;
rp43 <= 0+5p;
rp44 <= 0+5p;
rp45 <= 0+5p;
rp46 <= 0+5p;

/* CONSTRAINT 6
*  Demand at depot i on day t
*  tr_it <= D_it
*        for i=Glasgow to Plymouth, t=Monday to Saturday
*  Cells AB71:AE76
*/
tr11 <= 100;
tr12 <= 150;
tr13 <= 135;
tr14 <= 83;
tr15 <= 120;
tr16 <= 230;

tr21 <= 250;
tr22 <= 143;
tr23 <= 80;
tr24 <= 225;
tr25 <= 210;
tr26 <= 98;

tr31 <= 95;
tr32 <= 195;
tr33 <= 242;
tr34 <= 111;
tr35 <= 70;
tr36 <= 124;

tr41 <= 160;
tr42 <= 99;
tr43 <= 55;
tr44 <= 96;
tr45 <= 115;
tr46 <= 80;

/* CONSTRAINT 7
* Total number of cars equals number hired out from all depots on Monday for
3 days, plus those on Tuesday for 2 or 3 days, plus all damaged and undamaged
cars in depots at the beginning of Wednesday
*  SUM[i = Glasgow to Plymouth] (0.25 tr_i1 + 0.45 tr_i2 + nu_i3 + nd_i3) = n
*  Cells AB83
*/
0.25tr11 + 0.45tr12 + nu13 + nd13 +
0.25tr21 + 0.45tr22 + nu23 + nd23 +
0.25tr31 + 0.45tr32 + nu33 + nd33 +
0.25tr41 + 0.45tr42 + nu43 + nd43 = n;


/* Car Rental 2 constraints */
/* CONSTRAINT 8,9
* Second Expansion has to follow the first one
* B1 >= B2
* Cells AB95,AF95
* M1 >= M2
* Cells AB100,AF100
*/
b1 >= b2;
m1 >= m2;

/* CONSTRAINT 10
* Company can only choose 3 options maximum
* B1 + B2 + M1 + M2 + P <= 3
* Cells AB105
*/
b1 + b2 + m1 + m2 + p <=3;


//If required, Uncomment for CAR RENTAL 1//
//b1 + b2 + m1 + m2 + p <= 0;
//Uncomment for car rental 1//


/******************************** Variable bounds ********************************/

//Expansion decisions have to be binary as they can only be active or not
bin b1, b2, m1, m2, p;

//As stated in reference model, integrality of cars is not required
