-- #########################################################################
-- 1. create table MWIV_ORT here
CREATE TABLE "MWIV_ORT"
  (
    "ORTID"            		NUMBER(11,0) NOT NULL ENABLE,
    "ORTNAME"               VARCHAR2(255 BYTE) NOT NULL ENABLE,
	"PLZ"					VARCHAR2(255 BYTE) NOT NULL ENABLE,
    "SYSTEM"             	NUMBER(1,0) NOT NULL DEFAULT -1 ENABLE,
	"OFDB" 					VARCHAR2(255 BYTE),
    "ANGELEGTAM" 			DATE NOT NULL ENABLE,
    "ANGELEGTVON"    		VARCHAR2(255 BYTE) NOT NULL, 
    "ZULETZTBEARBEITETVON" 	VARCHAR2(255 BYTE),
    "ZULETZTGEAENDERTAM" 	DATE,
    CONSTRAINT "PK_ORT" PRIMARY KEY ("ORTID")
  )
  TABLESPACE "MW" CACHE
;

-- insert sequenz-key for MWIV_ORT
insert into SYSSEQUENZ(sequenzid, name, letztebelegtenr, inkrement, 
  blockgroesse, systemds, angelegtam, transaktionsid, angelegtvon)
  values((select nvl(max(s.sequenzid),0) + 1 from SYSSEQUENZ s), 'MWIV_ORT:ORTID', 0, 1, 90, -1, '19.03.2018', 0, 1);

CREATE UNIQUE INDEX "UQ_ORT_PLZORT" ON "MWIV_ORT" ("ORTNAME", "PLZ") TABLESPACE "MW";
  
-- #########################################################################
-- 2. create table MWIV_KATEGORIE here
CREATE TABLE "MWIV_KATEGORIE"
  (
    "KATEGORIEID"           NUMBER(11,0) NOT NULL ENABLE,
    "KATEGORIENAME"         VARCHAR2(255 BYTE) NOT NULL ENABLE,
    "SYSTEM"             	NUMBER(1,0) NOT NULL DEFAULT -1 ENABLE,
	"OFDB" 					VARCHAR2(255 BYTE),
    "ANGELEGTAM" 			DATE NOT NULL ENABLE,
    "ANGELEGTVON"    		VARCHAR2(255 BYTE) NOT NULL,
    "ZULETZTBEARBEITETVON" 	VARCHAR2(255 BYTE),
    "ZULETZTGEAENDERTAM" 	DATE,
    CONSTRAINT "PK_KATEGORIE" PRIMARY KEY ("KATEGORIEID")
  )
  TABLESPACE "MW" CACHE
;

-- insert sequenz-key for MWIV_KATEGORIE
insert into SYSSEQUENZ(sequenzid, name, letztebelegtenr, inkrement, 
  blockgroesse, systemds, angelegtam, transaktionsid, angelegtvon)
  values((select nvl(max(s.sequenzid),0) + 1 from SYSSEQUENZ s), 'MWIV_KATEGORIE:KATEGORIEID', 0, 1, 90, -1, '19.03.2018', 0, 1);

CREATE UNIQUE INDEX "UQ_KATEGORIE_KNAME" ON "MWIV_KATEGORIE" ("KATEGORIENAME") TABLESPACE "MW";
  
-- #########################################################################  
-- 3. create table MWIV_GRUPPE here
  CREATE TABLE "MWIV_GRUPPE" 
   (
    "GRUPPEID" 				NUMBER(11,0) NOT NULL ENABLE, 
    "GRUPPENAME" 			VARCHAR2(255 BYTE) NOT NULL ENABLE, 
	"ORTID" 				NUMBER(11,0) NOT NULL ENABLE,
	"KATEGORIEID" 			NUMBER(11,0) NOT NULL ENABLE,
	"KALENDARID" 			NUMBER(11,0) NOT NULL ENABLE,
    "ANGELEGTVON" 			VARCHAR2(255 BYTE) NOT NULL,
    "ANGELEGTAM" 			DATE NOT NULL ENABLE, 
	"ZULETZTBEARBEITETVON" 	VARCHAR2(255 BYTE),
    "ZULETZTGEAENDERTAM" 	DATE,
	"SYSTEM"             	NUMBER(1,0) NOT NULL DEFAULT -1 ENABLE,
	"OFDB" 					VARCHAR2(255 BYTE),
    CONSTRAINT "PK_GRUPPE" PRIMARY KEY ("GRUPPEID")
   )
  TABLESPACE "MW" 
  ;
 
-- insert sequenz-key for MWIV_GRUPPE
insert into SYSSEQUENZ(sequenzid, name, letztebelegtenr, inkrement, 
  blockgroesse, systemds, angelegtam, transaktionsid, angelegtvon)
  values((select nvl(max(s.sequenzid),0) + 1 from SYSSEQUENZ s), 'MWIV_GRUPPE:GRUPPEID', 0, 1, 90, -1, '19.03.2018', 0, 1);
  
CREATE UNIQUE INDEX "UQ_GRUPPE_KALENDARID" ON "MWIV_GRUPPE" ("KALENDARID") TABLESPACE "MW";  

CREATE UNIQUE INDEX "UQ_GRUPPE_GNAME_ORT" ON "MWIV_GRUPPE" ("GRUPPENAME", "ORTID") TABLESPACE "MW"; 
  
-- add foreign keys  
  alter table MWIV_GRUPPE
  add	 CONSTRAINT "FK_GRUPPE_ORTID" FOREIGN KEY ("ORTID")	REFERENCES "MWIV_ORT" ("ORTID") ENABLE;   
  
  alter table MWIV_GRUPPE
  add CONSTRAINT "FK_GRUPPE_KATEGORIEID" FOREIGN KEY ("KATEGORIEID")	REFERENCES "MWIV_KATEGORIE" ("KATEGORIEID") ENABLE;  
  
--insert into TEST_PERSON(PersonId, Name, spaltestringmitdefault, spaltebooleanmitdefault, spalteintmitdefault, angelegtvon, angelegtam)
--values( (select nvl(max(p.personid), 0) + 1 from TEST_PERSON p), 'Mustermann', 'defaultString', -1, 42, 1, '20.05.2012');
--
---- update syssequenz
--update syssequenz set letztebelegtenr = 1 where name = 'TEST_PERSON:PERSONID';

