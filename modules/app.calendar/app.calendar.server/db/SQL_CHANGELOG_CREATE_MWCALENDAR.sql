-- #########################################################################
-- 1. create table CAL_ORT here
CREATE TABLE "CAL_ORT"
(
    "ORTID"            		NUMBER(11,0) NOT NULL ENABLE,
    "ORTNAME"               VARCHAR2(255 BYTE) NOT NULL ENABLE,
	"PLZ"					VARCHAR2(255 BYTE) NOT NULL ENABLE,
    "SYSTEM"             	NUMBER(1,0) DEFAULT -1 NOT NULL  ENABLE,
    "ANGELEGTAM" 			DATE NOT NULL ENABLE,
    "ANGELEGTVON"    		VARCHAR2(255 BYTE) NOT NULL,
    CONSTRAINT "PK_ORT" PRIMARY KEY ("ORTID")
);

-- insert sequenz-key for CAL_ORT
insert into SYSSEQUENZ(sequenzid, name, letztebelegtenr, inkrement, 
  systemds, angelegtam, angelegtvon)
  values((select nvl(max(s.sequenzid),0) + 1 from SYSSEQUENZ s), 'CAL_ORT:ORTID', 0, 1, -1, '19.03.2018', 1);

CREATE UNIQUE INDEX "UQ_ORT_PLZORT" ON "CAL_ORT" ("ORTNAME", "PLZ") TABLESPACE "MW";
  
-- #########################################################################
-- 2. create table CAL_KATEGORIE here
CREATE TABLE "CAL_KATEGORIE"
(
    "KATEGORIEID"           NUMBER(11,0) NOT NULL ENABLE,
    "KATEGORIENAME"         VARCHAR2(255 BYTE) NOT NULL ENABLE,
    "SYSTEM"             	NUMBER(1,0) DEFAULT -1 NOT NULL  ENABLE,
    "ANGELEGTAM" 			DATE NOT NULL ENABLE,
    "ANGELEGTVON"    		VARCHAR2(255 BYTE) NOT NULL,
    CONSTRAINT "PK_KATEGORIE" PRIMARY KEY ("KATEGORIEID")
);

-- insert sequenz-key for CAL_KATEGORIE
insert into SYSSEQUENZ(sequenzid, name, letztebelegtenr, inkrement, 
  systemds, angelegtam, angelegtvon)
  values((select nvl(max(s.sequenzid),0) + 1 from SYSSEQUENZ s), 'CAL_KATEGORIE:KATEGORIEID', 0, 1,  -1, '19.03.2018', 1);

CREATE UNIQUE INDEX "UQ_KATEGORIE_KNAME" ON "CAL_KATEGORIE" ("KATEGORIENAME");
  
-- #########################################################################  
-- 3. create table CAL_GRUPPE here
CREATE TABLE "CAL_GRUPPE" 
(
    "GRUPPEID" 				NUMBER(11,0) NOT NULL ENABLE, 
    "GRUPPENAME" 			VARCHAR2(255 BYTE) NOT NULL ENABLE, 
	"ORTID" 				NUMBER(11,0) NOT NULL ENABLE,
	"KATEGORIEID" 			NUMBER(11,0) NOT NULL ENABLE,
	"KALENDARID" 			NUMBER(11,0) NOT NULL ENABLE,
    "ANGELEGTVON" 			VARCHAR2(255 BYTE) NOT NULL,
    "ANGELEGTAM" 			DATE NOT NULL ENABLE, 
	"SYSTEM"             	NUMBER(1,0) DEFAULT -1 NOT NULL  ENABLE,
    CONSTRAINT "PK_GRUPPE" PRIMARY KEY ("GRUPPEID")
);
 
-- insert sequenz-key for CAL_GRUPPE
insert into SYSSEQUENZ(sequenzid, name, letztebelegtenr, inkrement, 
  systemds, angelegtam, angelegtvon)
  values((select nvl(max(s.sequenzid),0) + 1 from SYSSEQUENZ s), 'CAL_GRUPPE:GRUPPEID', 0, 1, -1, '19.03.2018', 1);
  
CREATE UNIQUE INDEX "UQ_GRUPPE_KALENDARID" ON "CAL_GRUPPE" ("KALENDARID");  

CREATE UNIQUE INDEX "UQ_GRUPPE_GNAME_ORT" ON "CAL_GRUPPE" ("GRUPPENAME", "ORTID"); 
  
-- add foreign keys  
alter table CAL_GRUPPE
  add	 CONSTRAINT "FK_GRUPPE_ORTID" FOREIGN KEY ("ORTID")	REFERENCES "CAL_ORT" ("ORTID") ENABLE;   
  
alter table CAL_GRUPPE
  add CONSTRAINT "FK_GRUPPE_KATEGORIEID" FOREIGN KEY ("KATEGORIEID")	REFERENCES "CAL_KATEGORIE" ("KATEGORIEID") ENABLE;  
  
-- DML: insert BenutzerBereich Kalendar 
Insert into BENUTZERBEREICHEDEF (BEARBEITEN,HINZUFUEGEN,LESEN,LOESCHEN,BESCHREIBUNG,NAME,
BEREICHSID, ANGELEGTAM,ANGELEGTVON,SYSTEM) 
values ('-1','-1','-1','-1','Benutzerbereich für die Anwendung MWCalendar','Kalendar',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'BenutzerBereicheDef:BereichsID'),
to_date('10.07.18','DD.MM.RR'),'0','-1');

commit;