-- #########################################################################
-- 1. create table MW_BENUTZERROLLE
CREATE TABLE "KD_RRE_PROD"."MW_BENUTZERROLLE"
  (
    "ROLLEID"            		NUMBER(11,0) NOT NULL ENABLE,
    "ROLLENAME"               VARCHAR2(255 BYTE) NOT NULL ENABLE,
    "HINZUFUEGEN" 			NUMBER(1,0) NOT NULL DEFAULT 0 ENABLE,
	"BEARBEITEN" 			NUMBER(1,0) NOT NULL DEFAULT 0 ENABLE,
	"LOESCHEN" 			NUMBER(1,0) NOT NULL DEFAULT 0 ENABLE,
	"SYSTEM"             	NUMBER(1,0) NOT NULL DEFAULT -1 ENABLE,
	"OFDB" 					VARCHAR2(255 BYTE),
    "ANGELEGTAM" 			DATE NOT NULL ENABLE,
    "ANGELEGTVON"    		VARCHAR2(255 BYTE) NOT NULL, 
    "ZULETZTBEARBEITETVON" 	VARCHAR2(255 BYTE),
    "ZULETZTGEAENDERTAM" 	DATE,
    CONSTRAINT "PK_BENUTZERROLLE" PRIMARY KEY ("ROLLEID")
  )
  TABLESPACE "MW" CACHE
;

-- insert sequenz-key for MW_BENUTZERROLLE
insert into SYSSEQUENZ(sequenzid, name, letztebelegtenr, inkrement, 
  blockgroesse, systemds, angelegtam, transaktionsid, angelegtvon)
  values((select nvl(max(s.sequenzid),0) + 1 from SYSSEQUENZ s), 'MW_BENUTZERROLLE:ROLLEID', 0, 1, 90, -1, '19.03.2018', 0, 1);

CREATE UNIQUE INDEX "KD_RRE_PROD"."UQ_BENROLLE_NAME" ON "KD_RRE_PROD"."MW_BENUTZERROLLE" ("ROLLENAME") TABLESPACE "MW"; 
  
-- #########################################################################  
-- 2. create table MWIV_GRUPPE2BENUTZER2ROLLE
CREATE TABLE "KD_RRE_PROD"."MWIV_GRUPPE2BENUTZER2ROLLE"
  (
    "DSID"            		NUMBER(11,0) NOT NULL ENABLE,
	"GRUPPEID" 				NUMBER(11,0) NOT NULL ENABLE,
	"BENUTZERID" 				NUMBER(11,0) NOT NULL ENABLE,
	"ROLLEID" 				NUMBER(11,0) NOT NULL ENABLE,
    "SYSTEM"             	NUMBER(1,0) NOT NULL DEFAULT -1 ENABLE,
	"OFDB" 					VARCHAR2(255 BYTE),
    "ANGELEGTAM" 			DATE NOT NULL ENABLE,
    "ANGELEGTVON"    		VARCHAR2(255 BYTE) NOT NULL, 
    "ZULETZTBEARBEITETVON" 	VARCHAR2(255 BYTE),
    "ZULETZTGEAENDERTAM" 	DATE,
    CONSTRAINT "PK_BENUTZERROLLE" PRIMARY KEY ("ROLLEID")
  )
  TABLESPACE "MW" CACHE
;

-- insert sequenz-key for MW_BENUTZERROLLE
insert into SYSSEQUENZ(sequenzid, name, letztebelegtenr, inkrement, 
  blockgroesse, systemds, angelegtam, transaktionsid, angelegtvon)
  values((select nvl(max(s.sequenzid),0) + 1 from SYSSEQUENZ s), 'MWIV_GRUPPE2BENUTZER2ROLLE:DSID', 0, 1, 90, -1, '20.03.2018', 0, 1);

CREATE UNIQUE INDEX "KD_RRE_PROD"."UQ_G2B2R_GBRID" ON "KD_RRE_PROD"."MWIV_GRUPPE2BENUTZER2ROLLE" ("GRUPPEID", "BENUTZERID", "ROLLEID") TABLESPACE "MW"; 
  
alter table MWIV_GRUPPE2BENUTZER2ROLLE
  add CONSTRAINT "FK_G2B2R_GRUPPEID" FOREIGN KEY ("GRUPPEID")	REFERENCES "KD_RRE_PROD"."MWIV_GRUPPE" ("GRUPPEID") ENABLE;  

alter table MWIV_GRUPPE2BENUTZER2ROLLE
  add CONSTRAINT "FK_G2B2R_BENUTZERID" FOREIGN KEY ("BENUTZERID")	REFERENCES "KD_RRE_PROD"."BENUTZERDEF" ("DSID") ENABLE;  

alter table MWIV_GRUPPE2BENUTZER2ROLLE
  add CONSTRAINT "FK_G2B2R_ROLLEID" FOREIGN KEY ("ROLLEID")	REFERENCES "KD_RRE_PROD"."MW_BENUTZERROLLE" ("ROLLEID") ENABLE;    