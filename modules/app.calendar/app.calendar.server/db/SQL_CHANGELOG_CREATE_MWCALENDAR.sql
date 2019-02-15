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

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'BenutzerBereicheDef:BereichsID';

commit;

-- create tables views etc. for CAL_ORT
Insert into FX_TABDEF_K (TABELLE,ALIAS,DATENBANK,BEZEICHNUNG,ZEITTYP,EINDEUTIGERSCHLUESSEL,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,BEREICHSID,FULLCLASSNAME) 
values ('CAL_ORT','COrt','K','Ort',null,'DSID','MW',to_date('17.01.19','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_TabDef_K:DSID'),
(select b.BereichsID from BENUTZERBEREICHEDEF b where name = 'Kalendar'),'de.mw.mwdata.app.calendar.domain.Location');

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_TabDef_K:DSID';

Insert into FX_ANSICHTDEF_K (ANSICHT,BEZEICHNUNG,HINZUFUEGEN,BEARBEITEN,ENTFERNEN,LESEN,FILTER,SORTIEREN,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,URLPATH,BEREICHSID,REIHENFOLGE) 
values ('CAL_ORT','Ort','-1','-1','-1','-1','-1','-1','MW',to_date('25.01.19','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_AnsichtDef_K:DSID'),'location',
(select b.BereichsID from BENUTZERBEREICHEDEF b where name = 'Kalendar'),'10');

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_AnsichtDef_K:DSID';

Insert into FX_ANSICHTTAB_K (ANSICHT,REIHENFOLGE,TABAKEY,TABELLE,JOIN1SPALTEAKEY,JOIN2TABAKEY,JOIN2SPALTEAKEY,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,ANSICHTDEFID,TABDEFID, JOINTYP) 
values ('CAL_ORT','0','CAL_ORT',null,'x','x','x','MW',to_date('25.01.19','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_AnsichtTab_K:DSID'),
(select a.dsid from FX_AnsichtDef_K a where a.ansicht = 'CAL_ORT'),
(select a.dsid from FX_TabDef_K a where a.tabelle = 'CAL_ORT'), 'x');

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_AnsichtTab_K:DSID';

Insert into FX_MENUES_K (MENUEID,MENUE,ANZEIGENAME,TYP,EBENE,UNTERMENUEVON,LIZENZ,GRUPPE,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,BEREICHSID,AKTIV,ANSICHTDEFID,HAUPTMENUEID) 
values ('20','Cal','Kalendar','KNOTEN','0',null,null,'Kalendar','MW',to_date('25.01.19','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_Menues_K:DSID'),
(select b.BereichsID from BENUTZERBEREICHEDEF b where b.name = 'Kalendar'),'-1',null,null);

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_Menues_K:DSID';

Insert into FX_MENUES_K (MENUEID,MENUE,ANZEIGENAME,TYP,EBENE,UNTERMENUEVON,LIZENZ,GRUPPE,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,BEREICHSID,AKTIV,ANSICHTDEFID,HAUPTMENUEID) 
values ('2010','CalOrt','Orte','ANSICHT','1','Kalendar',null,null,'MW',to_date('28.01.19','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_Menues_K:DSID'),
(select BereichsId from BenutzerBereicheDef where NAME = 'Kalendar'),'-1',
(select dsid from FX_AnsichtDef_K where URLPATH = 'location'),
(select dsid from FX_Menues_K where anzeigename = 'Kalendar'));

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_Menues_K:DSID';

-- ***********************************
-- Datasets for TabSpeig / FX_AnsichtSpalten for CAL_ORT.ORTID
-- ***********************************

Insert into FX_TABSPEIG_K (REIHENFOLGE,SPALTE,SPALTENKOPF,DBDATENTYP,PS,EINDEUTIG,EINGABENOTWENDIG,BEARBERLAUBT,SYSTEMWERT,DEFAULTWERT,MINIMUM,MAXIMUM,SPALTENTYP,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,TABDEFID) 
values ('1','ORTID','Ort-ID','LONGINTEGER','-1',null,'-1','0','-1',null,null,null,'LaufendeNummer','MW',to_date('29.01.19','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_TabSpEig_K:DSID'),
(select dsid from FX_TabDef_K where tabelle = 'CAL_ORT'));

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_TabSpEig_K:DSID';

-- insert FX_AnsichtSpalten datasets for 
Insert into FX_ANSICHTSPALTEN_K (INDEXGRID,TABAKEY,SPALTEAKEY,INGRIDANZEIGEN,INGRIDLADEN,FILTER,EINGABENOTWENDIG,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,ANSICHTDEFID,ANSICHTTABID,TABSPEIGID) values ('1','CAL_ORT','ORTID','-1','-1','-1',null,'MW',to_date('29.01.19','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_AnsichtSpalten_K:DSID'),
(select dsid from FX_AnsichtDef_K where ansicht = 'CAL_ORT'),
(select dsid from FX_AnsichtTab_K where ansicht = 'CAL_ORT'),
(select FX_TabSpEig_K.dsid from FX_TabSpEig_K join FX_TabDef_K on FX_TabSpEig_K.TABDEFID = FX_TabDef_K.DSID 
where FX_TabDef_K.tabelle = 'CAL_ORT' and FX_TabSpEig_K.spalte = 'ORTID'));

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_AnsichtSpalten_K:DSID';

commit;

-- ***********************************
-- Datasets for TabSpeig / FX_AnsichtSpalten for CAL_ORT.ORTNAME
-- ***********************************

Insert into FX_TABSPEIG_K (REIHENFOLGE,SPALTE,SPALTENKOPF,DBDATENTYP,PS,EINDEUTIG,EINGABENOTWENDIG,BEARBERLAUBT,SYSTEMWERT,DEFAULTWERT,MINIMUM,MAXIMUM,SPALTENTYP,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,TABDEFID) 
values ('2','ORTNAME','Ort','STRING','0','10','-1','-1','-1',null,null,null,null,'MW',to_date('06.02.19','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_TabSpEig_K:DSID'),
(select dsid from FX_TabDef_K where tabelle = 'CAL_ORT'));

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_TabSpEig_K:DSID';

-- insert FX_AnsichtSpalten datasets for 
Insert into FX_ANSICHTSPALTEN_K (INDEXGRID,TABAKEY,SPALTEAKEY,INGRIDANZEIGEN,INGRIDLADEN,FILTER,EINGABENOTWENDIG,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,ANSICHTDEFID,ANSICHTTABID,TABSPEIGID) 
values ('1','CAL_ORT','ORTNAME','-1','-1','-1','-1','MW',to_date('06.02.19','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_AnsichtSpalten_K:DSID'),
(select dsid from FX_AnsichtDef_K where ansicht = 'CAL_ORT'),
(select dsid from FX_AnsichtTab_K where ansicht = 'CAL_ORT'),
(select FX_TabSpEig_K.dsid from FX_TabSpEig_K join FX_TabDef_K on FX_TabSpEig_K.TABDEFID = FX_TabDef_K.DSID 
where FX_TabDef_K.tabelle = 'CAL_ORT' and FX_TabSpEig_K.spalte = 'ORTNAME'));

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_AnsichtSpalten_K:DSID';

commit;