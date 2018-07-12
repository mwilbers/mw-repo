-- #########################################################################
-- 3. create sequences
CREATE TABLE SysSequenz (
	SEQUENZID NUMBER(11,0) NOT NULL,
	NAME VARCHAR(255) NOT NULL,
	LETZTEBELEGTENR NUMBER(11,0) NOT NULL,
	INKREMENT NUMBER(11,0) NOT NULL,
	system NUMERIC(1) DEFAULT -1 NOT NULL,
	BESCHREIBUNG VARCHAR(255), 
	ZULETZTBEARBEITETVON NUMBER(11,0),
	ZULETZTGEAENDERTAM TIMESTAMP,
	GESPERRTVONLOGINID NUMBER(11,0),
	SYSTEMDS NUMERIC(1) DEFAULT 0,
	angelegtAm TIMESTAMP NOT NULL,
	angelegtVon VARCHAR(255) NOT NULL,
	CONSTRAINT SEQUENZ_KEY_PK PRIMARY KEY (SEQUENZID)
);

CREATE UNIQUE INDEX IDX_SysSequenz_NAME ON SysSequenz (NAME);


-- 4. create table BenutzerBereicheDef
CREATE TABLE BENUTZERBEREICHEDEF (
	BereichsId NUMBER(11,0) NOT NULL,
	NAME VARCHAR(255) NOT NULL,
	BEARBEITEN NUMERIC(1,0) DEFAULT 0,
	BESCHREIBUNG VARCHAR(1000), 
	HINZUFUEGEN NUMERIC(1,0) DEFAULT 0,
	LESEN NUMERIC(1,0) DEFAULT 0,
	LOESCHEN NUMERIC(1,0) DEFAULT 0,
	angelegtAm TIMESTAMP NOT NULL,
	angelegtVon VARCHAR(255) NOT NULL,
	system NUMERIC(1) DEFAULT -1 NOT NULL,
	CONSTRAINT BENUTZERBEREICHEDEF_KEY_PK PRIMARY KEY (BereichsId)
);

CREATE UNIQUE INDEX IDX_BenBereicheDef_NAME ON BenutzerBereicheDef (NAME);

-- 5. create table BenutzerDef 
CREATE TABLE BenutzerDef (
	DSID NUMBER(11,0) NOT NULL,
	BENUTZERNAME VARCHAR(255) NOT NULL,
	PASSWORT VARCHAR(255) NOT NULL,
	DEAKTIVIERT NUMERIC(1,0) DEFAULT 0 NOT NULL,
	OBJEKTROLLENID INTEGER,
	angelegtAm TIMESTAMP NOT NULL,
	angelegtVon VARCHAR(255) NOT NULL,
	system NUMERIC(1) DEFAULT -1 NOT NULL,
	CONSTRAINT BENUTZERDEF_KEY_PK PRIMARY KEY (DSID)
);

CREATE UNIQUE INDEX IDX_BenutzerDef_BENNAME ON BenutzerDef (BENUTZERNAME);

-- 6. create table BenutzerRechte 
CREATE TABLE BenutzerRechte (
	DSID NUMBER(11,0) NOT NULL,
	BENUTZERID INTEGER NOT NULL,
	system NUMERIC(1) DEFAULT -1 NOT NULL,
	BEREICHSID INTEGER NOT NULL,
	BEARBEITEN NUMERIC(1,0) DEFAULT -1,
	HINZUFUEGEN NUMERIC(1,0) DEFAULT -1,
	LOESCHEN NUMERIC(1,0) DEFAULT 0,
	angelegtAm TIMESTAMP NOT NULL,
	angelegtVon VARCHAR(255) NOT NULL,
	CONSTRAINT BENUTZERRECHTE_KEY_PK PRIMARY KEY (DSID)
);

ALTER TABLE BenutzerRechte ADD FOREIGN KEY (BENUTZERID) REFERENCES BenutzerDef(DSID);
ALTER TABLE BenutzerRechte ADD FOREIGN KEY (BEREICHSID) REFERENCES BenutzerBereicheDef(BereichsId);

CREATE UNIQUE INDEX IDX_BenRechte_BenBer ON BenutzerRechte (BENUTZERID,BEREICHSID);

-- 7. create table FX_TabDef_K 
CREATE TABLE FX_TabDef_K (
	DSID NUMBER(11,0) NOT NULL,
	TABELLE VARCHAR(255) NOT NULL,
	ALIAS VARCHAR(255) NOT NULL,
	DATENBANK VARCHAR(255) NOT NULL,
	BEZEICHNUNG VARCHAR(255), 
	BEREICHSID NUMBER(11,0) NOT NULL,
	ZEITTYP VARCHAR(255), 
	EINDEUTIGERSCHLUESSEL VARCHAR(255) NOT NULL,
	FULLCLASSNAME VARCHAR(255), 
	angelegtAm TIMESTAMP NOT NULL,
	angelegtVon VARCHAR(255) NOT NULL,
	system NUMERIC(1) DEFAULT -1 NOT NULL,
	CONSTRAINT TABDEF_KEY_PK PRIMARY KEY (DSID)
);

ALTER TABLE FX_TabDef_K ADD FOREIGN KEY (BEREICHSID) REFERENCES BenutzerBereicheDef(BereichsId);

CREATE UNIQUE INDEX IDX_FX_TabDef_TABELLE ON FX_TabDef_K (TABELLE);
CREATE UNIQUE INDEX IDX_FX_TabDef_ALIAS ON FX_TabDef_K (ALIAS);

-- 8. create table FX_TAbSpEig_K 
CREATE TABLE FX_TabSpEig_K (
	DSID NUMBER(11,0) NOT NULL,
	TABDEFID NUMBER(11,0) NOT NULL,
	REIHENFOLGE NUMBER(11,0) NOT NULL,
	SPALTE VARCHAR(255) NOT NULL,
	DBDATENTYP VARCHAR(255) NOT NULL,
	PS NUMERIC(1) DEFAULT 0,
	EINDEUTIG NUMBER(11,0) DEFAULT 0,
	EINGABENOTWENDIG NUMERIC(1) DEFAULT 0,
	BEARBERLAUBT NUMERIC(1) DEFAULT -1,
	SYSTEMWERT NUMERIC(1) DEFAULT 0,
	DEFAULTWERT VARCHAR(255), 
	MAXIMUM VARCHAR(255), 
	MINIMUM VARCHAR(255), 
	SPALTENKOPF VARCHAR(255) NOT NULL,
	SPALTENTYP VARCHAR(255), 
	angelegtAm TIMESTAMP NOT NULL,
	angelegtVon VARCHAR(255) NOT NULL,
	system NUMERIC(1) DEFAULT -1 NOT NULL,
	CONSTRAINT TABSPEIG_KEY_PK PRIMARY KEY (DSID)
);

ALTER TABLE FX_TabSpEig_K ADD FOREIGN KEY (TABDEFID) REFERENCES FX_TabDef_K(DSID);

CREATE UNIQUE INDEX IDX_FX_TabSp_TID_SPL ON FX_TabSpEig_K (TABDEFID, SPALTE);
CREATE UNIQUE INDEX IDX_FX_TabSp_TID_REIH ON FX_TabSpEig_K (TABDEFID, REIHENFOLGE);

CREATE TABLE FX_TabBez_K (
	DSID NUMBER(11,0) NOT NULL,
	TABELLEEIG VARCHAR(255) NOT NULL,
	TABELLEDEF VARCHAR(255) NOT NULL,
	DEFSCHLUESSEL VARCHAR(255) NOT NULL,
	EIGSCHLUESSEL VARCHAR(255) NOT NULL,
	OPERATOR VARCHAR(255) NOT NULL,
	INDB NUMBER(11,0) NOT NULL,
	angelegtAm TIMESTAMP NOT NULL,
	angelegtVon VARCHAR(255) NOT NULL,
	system NUMERIC(1) DEFAULT -1 NOT NULL,
	CONSTRAINT TABBEZ_KEY_PK PRIMARY KEY (DSID)
);

-- 9. create table FX_AnsichtDef_K
CREATE TABLE FX_ANSICHTDEF_K (
	DSID NUMBER(11,0) NOT NULL,
	ANSICHT VARCHAR(255) NOT NULL,
	BEREICHSID NUMBER(11,0) NOT NULL,
	BEZEICHNUNG VARCHAR(255), 
	HINZUFUEGEN NUMERIC(1,0) DEFAULT 0,
	BEARBEITEN NUMERIC(1,0) DEFAULT 0,
	ENTFERNEN NUMERIC(1,0) DEFAULT 0,
	LESEN NUMERIC(1,0) DEFAULT 0,
	FILTER NUMERIC(1,0) DEFAULT 0,
	SORTIEREN NUMERIC(1,0) DEFAULT 0,
	URLPATH VARCHAR(255), 
	angelegtAm TIMESTAMP NOT NULL,
	angelegtVon VARCHAR(255) NOT NULL,
	system NUMERIC(1) DEFAULT -1 NOT NULL,
	REIHENFOLGE INTEGER NULL,
	CONSTRAINT ANSICHTDEF_KEY_PK PRIMARY KEY (DSID)
);

ALTER TABLE FX_ANSICHTDEF_K ADD FOREIGN KEY (BEREICHSID) REFERENCES BenutzerBereicheDef(BereichsId);
CREATE UNIQUE INDEX IDX_FX_ANSDEF_ANS_of ON FX_ANSICHTDEF_K (ANSICHT);

-- 9. create table FX_AnsichtTab_K
CREATE TABLE FX_ANSICHTTAB_K (
	 DSID NUMBER(11,0) NOT NULL,
	 ANSICHT VARCHAR(255) NOT NULL,
	 TABAKEY VARCHAR(255) NOT NULL,
	 TABELLE VARCHAR(255), JOINTYP VARCHAR(255) NOT NULL,
	 JOIN1SPALTEAKEY VARCHAR(255) NOT NULL,
	 JOIN2TABAKEY VARCHAR(255) NOT NULL,
	 JOIN2SPALTEAKEY VARCHAR(255) NOT NULL,
	 ANSICHTDEFID NUMBER(11,0) NOT NULL,
	 TABDEFID NUMBER(11,0) NOT NULL,
	 REIHENFOLGE INTEGER NOT NULL,
	 angelegtAm TIMESTAMP NOT NULL,
	 angelegtVon VARCHAR(255) NOT NULL,
	 system NUMERIC(1) DEFAULT -1 NOT NULL,
	 CONSTRAINT ANSTAB_KEY_PK PRIMARY KEY (DSID) 
);

ALTER TABLE FX_ANSICHTTAB_K ADD FOREIGN KEY (ANSICHTDEFID) REFERENCES FX_ANSICHTDEF_K(DSID);
ALTER TABLE FX_ANSICHTTAB_K ADD FOREIGN KEY (TABDEFID) REFERENCES FX_TabDef_K(DSID);
CREATE UNIQUE INDEX IDX_ANSICHTTAB_ANSTABAKEY ON FX_ANSICHTTAB_K (TABAKEY, ANSICHTDEFID);

-- 11. create table FX_AnsichtSpalten_K
CREATE TABLE FX_ANSICHTSPALTEN_K (
	DSID NUMBER(11,0) NOT NULL,
	INDEXGRID INTEGER NOT NULL,
	TABAKEY VARCHAR(255) NOT NULL,
	SPALTEAKEY VARCHAR(255) NOT NULL,
	INGRIDANZEIGEN NUMERIC(11) DEFAULT 0,
	INGRIDLADEN NUMERIC(11) DEFAULT 0,
	FILTER NUMERIC(11) DEFAULT 0,
	ANZAHLNACHKOMMASTELLEN INTEGER DEFAULT 0,
	BEARBHINZUFZUGELASSEN NUMERIC(11) DEFAULT 0,
	BEARBZUGELASSEN NUMERIC(1) DEFAULT 0,
	EINGABENOTWENDIG NUMERIC(11) DEFAULT 0,
	ANSICHTSUCHEN VARCHAR(255), 
	SUCHWERTAUSTABAKEY VARCHAR(255), 
	SUCHWERTAUSSPALTEAKEY VARCHAR(255), 
	WHERETABAKEY VARCHAR(255), 
	WHERESPALTEAKEY VARCHAR(255), 
	VERDECKENDURCHTABAKEY VARCHAR(255), 
	VERDECKENDURCHSPALTEAKEY VARCHAR(255), 
	angelegtAm TIMESTAMP NOT NULL,
	angelegtVon VARCHAR(255) NOT NULL,
	system NUMERIC(1) DEFAULT -1 NOT NULL,
	ANSICHTDEFID NUMBER(11,0) NOT NULL,
	TABSPEIGID NUMBER(11,0) NOT NULL,
	ANSICHTTABID NUMBER(11,0) NOT NULL,
	CONSTRAINT ANSICHTSPALTEN_KEY_PK PRIMARY KEY (DSID)
);

CREATE UNIQUE INDEX IDX_FX_ANSSP_TABKSPK ON FX_ANSICHTSPALTEN_K (TABAKEY, SPALTEAKEY);
ALTER TABLE FX_ANSICHTSPALTEN_K ADD FOREIGN KEY (ANSICHTTABID) REFERENCES FX_ANSICHTTAB_K(DSID);
ALTER TABLE FX_ANSICHTSPALTEN_K ADD FOREIGN KEY (TABSPEIGID) REFERENCES FX_TabSpEig_K(DSID);

-- 12. create table FX_Menue_K
CREATE TABLE FX_MENUES_K (
	DSID NUMBER(11,0) NOT NULL,
	MENUEID NUMBER(11,0) NOT NULL,
	MENUE VARCHAR(255) NOT NULL,
	UNTERMENUEVON VARCHAR(255), 
	HAUPTMENUEID NUMBER(11,0),
	ANZEIGENAME VARCHAR(255), 
	TYP VARCHAR(255) NOT NULL,
	EBENE INTEGER NOT NULL,
	BEREICHSID NUMBER(11,0),
	ANSICHTDEFID NUMBER(11,0),
	LIZENZ VARCHAR(255), 
	GRUPPE VARCHAR(255), 
	AKTIV NUMERIC(1,0) DEFAULT -1,
	angelegtAm TIMESTAMP NOT NULL,
	angelegtVon VARCHAR(255) NOT NULL,
	system NUMERIC(1) DEFAULT -1 NOT NULL,
	URLPATH VARCHAR(255),
	CONSTRAINT MENUES_KEY_PK PRIMARY KEY (DSID)
);

ALTER TABLE FX_MENUES_K ADD FOREIGN KEY (HAUPTMENUEID) REFERENCES FX_MENUES_K(DSID);
ALTER TABLE FX_MENUES_K ADD FOREIGN KEY (BEREICHSID) REFERENCES BenutzerBereicheDef(BereichsId);
ALTER TABLE FX_MENUES_K ADD FOREIGN KEY (ANSICHTDEFID) REFERENCES FX_ANSICHTDEF_K(DSID);

-- 13. create table FX_AnsichtOrderBy_K 
CREATE TABLE FX_ANSICHTORDERBY_K (
	DSID NUMBER(11,0) NOT NULL,
	ANSICHT VARCHAR(255) NOT NULL,
	ANSICHTTABID NUMBER(11,0) NOT NULL,
	ANSICHTSPALTENID NUMBER(11,0) NOT NULL,
	AUFSTEIGEND NUMERIC(1) DEFAULT -1,
	REIHENFOLGE INT NOT NULL,
	TABAKEY VARCHAR(255) NOT NULL,
	SPALTEAKEY VARCHAR(255) NOT NULL,
	angelegtAm TIMESTAMP NOT NULL,
	angelegtVon VARCHAR(255) NOT NULL,
	system NUMERIC(1) DEFAULT -1 NOT NULL,
	CONSTRAINT ANSORDER_KEY_PK PRIMARY KEY (DSID) 
);

ALTER TABLE FX_ANSICHTORDERBY_K ADD FOREIGN KEY (ANSICHTTABID) REFERENCES FX_ANSICHTTAB_K(DSID);
ALTER TABLE FX_ANSICHTORDERBY_K ADD FOREIGN KEY (ANSICHTSPALTENID) REFERENCES FX_ANSICHTSPALTEN_K(DSID);

-- 14. create table ADAnsichtDef
CREATE TABLE ADANSICHTENDEF (
	DSID NUMBER(11,0) NOT NULL,
	ANSICHT VARCHAR(255) NOT NULL,
	BEZEICHNUNG VARCHAR(255) NOT NULL,
	ANSICHTID NUMBER(11,0) NOT NULL,
	DEFAULTANSICHT NUMERIC(1) DEFAULT -1,
	angelegtAm TIMESTAMP NOT NULL,
	angelegtVon VARCHAR(255) NOT NULL,
	system NUMERIC(1) DEFAULT -1 NOT NULL,
	CONSTRAINT ADANSICHT_KEY_PK PRIMARY KEY (DSID) 
);

ALTER TABLE ADANSICHTENDEF ADD FOREIGN KEY (ANSICHTID) REFERENCES FX_ANSICHTDEF_K(DSID);

-- 15. create table ADAnsichtSpalten 
CREATE TABLE ADANSICHTSPALTEN (
	DSID NUMBER(11,0) NOT NULL,
	SPALTEASKEY VARCHAR(255) NOT NULL,
	ANSICHTID NUMBER(11,0) NOT NULL,
	ANSICHTSPALTENDEFID NUMBER(11,0) NOT NULL,
	angelegtAm TIMESTAMP NOT NULL,
	angelegtVon VARCHAR(255) NOT NULL,
	system NUMERIC(1) DEFAULT -1 NOT NULL,
	CONSTRAINT ADSPALTEN_KEY_PK PRIMARY KEY (DSID) 
);

-- DML: sequences
INSERT INTO SysSequenz(SEQUENZID, NAME, LETZTEBELEGTENR, INKREMENT, BESCHREIBUNG, ZULETZTBEARBEITETVON, ZULETZTGEAENDERTAM, SYSTEMDS, angelegtAm, angelegtVon) 
values(1, 'SysSequenz:SequenzID', 0, 1, 'Sequenzkey für die Sequenztabelle', 0, '22.06.2018', -1, to_date('22.06.2018','DD.MM.RR'), 0);
Insert into SYSSEQUENZ (SEQUENZID,NAME,LETZTEBELEGTENR,INKREMENT,GESPERRTVONLOGINID,SYSTEMDS,ANGELEGTAM,BESCHREIBUNG,ANGELEGTVON,SYSTEM) 
values ('2','BenutzerBereicheDef:BereichsID','0','1',null,'-1',to_date('22.06.2018','DD.MM.RR'),null,'1','-1');
Insert into SYSSEQUENZ (SEQUENZID,NAME,LETZTEBELEGTENR,INKREMENT,GESPERRTVONLOGINID,SYSTEMDS,ANGELEGTAM,BESCHREIBUNG,ANGELEGTVON,SYSTEM) 
values ('3','FX_AnsichtDef_K:DSID','0','1',null,'-1',to_date('22.06.2018','DD.MM.RR'),null,'1','-1');
Insert into SYSSEQUENZ (SEQUENZID,NAME,LETZTEBELEGTENR,INKREMENT,GESPERRTVONLOGINID,SYSTEMDS,ANGELEGTAM,BESCHREIBUNG,ANGELEGTVON,SYSTEM) 
values ('4','FX_AnsichtTab_K:DSID','0','1',null,'-1',to_date('22.06.18','DD.MM.RR'),null,'1','-1');
Insert into SYSSEQUENZ (SEQUENZID,NAME,LETZTEBELEGTENR,INKREMENT,GESPERRTVONLOGINID,SYSTEMDS,ANGELEGTAM,BESCHREIBUNG,ANGELEGTVON,SYSTEM) 
values ('5','ADAnsichtSpalten:DSID','0','1',null,'-1',to_date('22.06.18','DD.MM.RR'),null,'1','-1');
Insert into SYSSEQUENZ (SEQUENZID,NAME,LETZTEBELEGTENR,INKREMENT,GESPERRTVONLOGINID,SYSTEMDS,ANGELEGTAM,BESCHREIBUNG,ANGELEGTVON,SYSTEM) 
values ('6','Benutzerdef:DSID','0','1',null,'-1',to_date('22.06.18','DD.MM.RR'),null,'1','-1');
Insert into SYSSEQUENZ (SEQUENZID,NAME,LETZTEBELEGTENR,INKREMENT,GESPERRTVONLOGINID,SYSTEMDS,ANGELEGTAM,BESCHREIBUNG,ANGELEGTVON,SYSTEM) 
values ('7','FX_AnsichtOrderBy_K:DSID','0','1',null,'-1',to_date('22.06.18','DD.MM.RR'),null,'1','-1');
Insert into SYSSEQUENZ (SEQUENZID,NAME,LETZTEBELEGTENR,INKREMENT,GESPERRTVONLOGINID,SYSTEMDS,ANGELEGTAM,BESCHREIBUNG,ANGELEGTVON,SYSTEM) 
values ('8','FX_AnsichtSpalten_K:DSID','0','1',null,'-1',to_date('22.06.18','DD.MM.RR'),null,'1','-1');
Insert into SYSSEQUENZ (SEQUENZID,NAME,LETZTEBELEGTENR,INKREMENT,GESPERRTVONLOGINID,SYSTEMDS,ANGELEGTAM,BESCHREIBUNG,ANGELEGTVON,SYSTEM) 
values ('9','FX_Menues_K:DSID','0','1',null,'-1',to_date('22.06.18','DD.MM.RR'),null,'1','-1');
Insert into SYSSEQUENZ (SEQUENZID,NAME,LETZTEBELEGTENR,INKREMENT,GESPERRTVONLOGINID,SYSTEMDS,ANGELEGTAM,BESCHREIBUNG,ANGELEGTVON,SYSTEM) 
values ('10','FX_TabBez_K:DSID','0','1',null,'-1',to_date('22.06.18','DD.MM.RR'),null,'1','-1');
Insert into SYSSEQUENZ (SEQUENZID,NAME,LETZTEBELEGTENR,INKREMENT,GESPERRTVONLOGINID,SYSTEMDS,ANGELEGTAM,BESCHREIBUNG,ANGELEGTVON,SYSTEM) 
values ('11','FX_TabDef_K:DSID','0','1',null,'-1',to_date('22.06.18','DD.MM.RR'),null,'1','-1');
Insert into SYSSEQUENZ (SEQUENZID,NAME,LETZTEBELEGTENR,INKREMENT,GESPERRTVONLOGINID,SYSTEMDS,ANGELEGTAM,BESCHREIBUNG,ANGELEGTVON,SYSTEM) 
values ('12','FX_TabSpEig_K:DSID','0','1',null,'-1',to_date('22.06.18','DD.MM.RR'),null,'1','-1');

-- DML: insert BenutzerBereich Administrator 
Insert into BENUTZERBEREICHEDEF (BEARBEITEN,HINZUFUEGEN,LESEN,LOESCHEN,BESCHREIBUNG,NAME,
BEREICHSID, ANGELEGTAM,ANGELEGTVON,SYSTEM) 
values ('-1','-1','-1','-1','Benutzerverwaltung, allgemeine Einstellungen, allgemeine Daten','Administrator',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'BenutzerBereicheDef:BereichsID'),
to_date('22.06.18','DD.MM.RR'),'0','-1');

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'BenutzerBereicheDef:BereichsID';

-- DML: insert TabDefs
Insert into FX_TABDEF_K (TABELLE,ALIAS,DATENBANK,BEZEICHNUNG,ZEITTYP,EINDEUTIGERSCHLUESSEL,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,BEREICHSID,FULLCLASSNAME) 
values ('FX_Menues_K','FXMenK','K','Sys_FX_Menues_K',null,'DSID','MW',to_date('05.04.06','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_TabDef_K:DSID'),
(select b.BereichsID from BENUTZERBEREICHEDEF b where name = 'Administrator'),'de.mw.mwdata.ofdb.domain.impl.Menue');

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_TabDef_K:DSID';

Insert into FX_TABDEF_K (TABELLE,ALIAS,DATENBANK,BEZEICHNUNG,ZEITTYP,EINDEUTIGERSCHLUESSEL,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,BEREICHSID,FULLCLASSNAME) 
values ('FX_TabDef_K','FXTDK','K','Sys_FX_TabDef_K',null,'DSID','MW',to_date('05.04.06','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_TabDef_K:DSID'), 
(select b.BereichsID from BENUTZERBEREICHEDEF b where name = 'Administrator'), 'de.mw.mwdata.ofdb.domain.impl.TabDef');

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_TabDef_K:DSID';

Insert into FX_TABDEF_K (TABELLE,ALIAS,DATENBANK,BEZEICHNUNG,ZEITTYP,EINDEUTIGERSCHLUESSEL,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,BEREICHSID,FULLCLASSNAME) 
values ('FX_TabSpEig_K','FXTSK','K','Sys_FX_TabSpeig_K',null,'DSID','MW',to_date('05.04.06','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_TabDef_K:DSID'), 
(select b.BereichsID from BENUTZERBEREICHEDEF b where name = 'Administrator'), 'de.mw.mwdata.ofdb.domain.impl.TabSpeig');

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_TabDef_K:DSID';

Insert into FX_TABDEF_K (TABELLE,ALIAS,DATENBANK,BEZEICHNUNG,ZEITTYP,EINDEUTIGERSCHLUESSEL,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,BEREICHSID,FULLCLASSNAME) 
values ('FX_AnsichtDef_K','FXADK','K','Sys_FX_AnsichtDef_K',null,'DSID','MW',to_date('05.04.06','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_TabDef_K:DSID'), 
(select b.BereichsID from BENUTZERBEREICHEDEF b where name = 'Administrator'), 'de.mw.mwdata.ofdb.domain.impl.AnsichtDef');

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_TabDef_K:DSID';

Insert into FX_TABDEF_K (TABELLE,ALIAS,DATENBANK,BEZEICHNUNG,ZEITTYP,EINDEUTIGERSCHLUESSEL,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,BEREICHSID,FULLCLASSNAME) 
values ('FX_AnsichtSpalten_K','FXASK','K','Sys_FX_AnsichtSpalten_K',null,'DSID','MW',to_date('05.04.06','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_TabDef_K:DSID'), 
(select b.BereichsID from BENUTZERBEREICHEDEF b where name = 'Administrator'), 'de.mw.mwdata.ofdb.domain.impl.AnsichtSpalten');

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_TabDef_K:DSID';

Insert into FX_TABDEF_K (TABELLE,ALIAS,DATENBANK,BEZEICHNUNG,ZEITTYP,EINDEUTIGERSCHLUESSEL,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,BEREICHSID,FULLCLASSNAME) 
values ('FX_AnsichtTab_K','FXATK','K','Sys_FX_AnsichtTab_K',null,'DSID','MW',to_date('05.04.06','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_TabDef_K:DSID'), 
(select b.BereichsID from BENUTZERBEREICHEDEF b where name = 'Administrator'), 'de.mw.mwdata.ofdb.domain.impl.AnsichtTab');

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_TabDef_K:DSID';

Insert into FX_TABDEF_K (TABELLE,ALIAS,DATENBANK,BEZEICHNUNG,ZEITTYP,EINDEUTIGERSCHLUESSEL,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,BEREICHSID,FULLCLASSNAME) 
values ('FX_AnsichtOrderBy_K','FXAOB','K','Sys_FX_AnsichtOrderBy_K',null,'DSID','MW',to_date('05.04.06','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_TabDef_K:DSID'), 
(select b.BereichsID from BENUTZERBEREICHEDEF b where name = 'Administrator'), 'de.mw.mwdata.ofdb.domain.impl.AnsichtOrderBy');

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_TabDef_K:DSID';

Insert into FX_TABDEF_K (TABELLE,ALIAS,DATENBANK,BEZEICHNUNG,ZEITTYP,EINDEUTIGERSCHLUESSEL,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,BEREICHSID,FULLCLASSNAME) 
values ('BenutzerBereicheDef','BenB','K','Rechte_Bereiche',null,'BereichsID','MW',to_date('30.08.02','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_TabDef_K:DSID'), 
(select b.BereichsID from BENUTZERBEREICHEDEF b where name = 'Administrator'), 'de.mw.mwdata.core.domain.BenutzerBereich');

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_TabDef_K:DSID';

Insert into FX_TABDEF_K (TABELLE,ALIAS,DATENBANK,BEZEICHNUNG,ZEITTYP,EINDEUTIGERSCHLUESSEL,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,BEREICHSID,FULLCLASSNAME) 
values ('BenutzerDef','BenD','K','Benutzer_Stammdaten',null,'DSID','MW',to_date('30.08.02','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_TabDef_K:DSID'), 
(select b.BereichsID from BENUTZERBEREICHEDEF b where name = 'Administrator'), 'de.mw.mwdata.core.domain.Benutzer');

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_TabDef_K:DSID';

Insert into FX_TABDEF_K (TABELLE,ALIAS,DATENBANK,BEZEICHNUNG,ZEITTYP,EINDEUTIGERSCHLUESSEL,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,BEREICHSID,FULLCLASSNAME) 
values ('BenutzerRechte','BenR','K','Benutzerrechte',null,'DSID','MW',to_date('07.06.01','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_TabDef_K:DSID'), 
(select b.BereichsID from BENUTZERBEREICHEDEF b where name = 'Administrator'), 'de.mw.mwdata.core.domain.BenutzerRecht');

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_TabDef_K:DSID';

-- INSERT FX_ANSICHTDEF datasets
Insert into FX_ANSICHTDEF_K (ANSICHT,BEZEICHNUNG,HINZUFUEGEN,BEARBEITEN,ENTFERNEN,LESEN,FILTER,SORTIEREN,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,URLPATH,BEREICHSID,REIHENFOLGE) 
values ('FX_AnsichtDef_K',null,'-1','-1','-1','-1','-1','-1','MW',to_date('02.07.18','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_AnsichtDef_K:DSID'),'ansichtDef',
(select b.BereichsID from BENUTZERBEREICHEDEF b where name = 'Administrator'),'30');

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_AnsichtDef_K:DSID';

Insert into FX_ANSICHTDEF_K (ANSICHT,BEZEICHNUNG,HINZUFUEGEN,BEARBEITEN,ENTFERNEN,LESEN,FILTER,SORTIEREN,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,URLPATH,BEREICHSID,REIHENFOLGE) 
values ('FX_AnsichtSpalten_K',null,'-1','-1','-1','-1','-1','-1','MW',to_date('02.07.18','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_AnsichtDef_K:DSID'),'ansichtSpalten',
(select b.BereichsID from BENUTZERBEREICHEDEF b where name = 'Administrator'),'40');

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_AnsichtDef_K:DSID';

Insert into FX_ANSICHTDEF_K (ANSICHT,BEZEICHNUNG,HINZUFUEGEN,BEARBEITEN,ENTFERNEN,LESEN,FILTER,SORTIEREN,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,URLPATH,BEREICHSID,REIHENFOLGE) 
values ('FX_AnsichtTab_K',null,'-1','-1','-1','-1','-1','-1','MW',to_date('02.07.18','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_AnsichtDef_K:DSID'),'ansichtTab',
(select b.BereichsID from BENUTZERBEREICHEDEF b where name = 'Administrator'),'45');

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_AnsichtDef_K:DSID';

Insert into FX_ANSICHTDEF_K (ANSICHT,BEZEICHNUNG,HINZUFUEGEN,BEARBEITEN,ENTFERNEN,LESEN,FILTER,SORTIEREN,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,URLPATH,BEREICHSID,REIHENFOLGE) 
values ('BenutzerBereicheDef',null,'-1','-1','-1','-1','-1','-1','MW',to_date('02.07.18','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_AnsichtDef_K:DSID'),'benutzerBereich',
(select b.BereichsID from BENUTZERBEREICHEDEF b where name = 'Administrator'),'5');

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_AnsichtDef_K:DSID';

Insert into FX_ANSICHTDEF_K (ANSICHT,BEZEICHNUNG,HINZUFUEGEN,BEARBEITEN,ENTFERNEN,LESEN,FILTER,SORTIEREN,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,URLPATH,BEREICHSID,REIHENFOLGE) 
values ('FX_Menues_K',null,'-1','-1','-1','-1','-1','-1','MW',to_date('02.07.18','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_AnsichtDef_K:DSID'),'menues',
(select b.BereichsID from BENUTZERBEREICHEDEF b where name = 'Administrator'),'50');

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_AnsichtDef_K:DSID';

Insert into FX_ANSICHTDEF_K (ANSICHT,BEZEICHNUNG,HINZUFUEGEN,BEARBEITEN,ENTFERNEN,LESEN,FILTER,SORTIEREN,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,URLPATH,BEREICHSID,REIHENFOLGE) 
values ('FX_TabDef_K',null,'-1','-1','-1','-1','-1','-1','MW',to_date('02.07.18','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_AnsichtDef_K:DSID'),'tabDef',
(select b.BereichsID from BENUTZERBEREICHEDEF b where name = 'Administrator'),'10');

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_AnsichtDef_K:DSID';

Insert into FX_ANSICHTDEF_K (ANSICHT,BEZEICHNUNG,HINZUFUEGEN,BEARBEITEN,ENTFERNEN,LESEN,FILTER,SORTIEREN,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,URLPATH,BEREICHSID,REIHENFOLGE) 
values ('FX_TabSpEig_K',null,'-1','-1','-1','-1','-1','-1','MW',to_date('02.07.18','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_AnsichtDef_K:DSID'),'tabSpeig',
(select b.BereichsID from BENUTZERBEREICHEDEF b where name = 'Administrator'),'20');

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_AnsichtDef_K:DSID';

Insert into FX_ANSICHTDEF_K (ANSICHT,BEZEICHNUNG,HINZUFUEGEN,BEARBEITEN,ENTFERNEN,LESEN,FILTER,SORTIEREN,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,URLPATH,BEREICHSID,REIHENFOLGE) 
values ('FX_AnsichtOrderBy_K',null,'-1','-1','-1','-1','-1','-1','MW',to_date('02.07.18','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_AnsichtDef_K:DSID'),'ansichtOrderBy',
(select b.BereichsID from BENUTZERBEREICHEDEF b where name = 'Administrator'),'20');

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_AnsichtDef_K:DSID';

-- INSERT FX_ANSICHTTAB_K datasets
Insert into FX_ANSICHTTAB_K (ANSICHT,REIHENFOLGE,TABAKEY,TABELLE,JOIN1SPALTEAKEY,JOIN2TABAKEY,JOIN2SPALTEAKEY,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,ANSICHTDEFID,TABDEFID, JOINTYP) 
values ('BenutzerBereicheDef','0','BenutzerBereicheDef',null,'x','x','x','MW',to_date('02.07.18','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_AnsichtDef_K:DSID'),
(select a.dsid from FX_AnsichtDef_K a where a.ansicht = 'BenutzerBereicheDef'),
(select a.dsid from FX_TabDef_K a where a.tabelle = 'BenutzerBereicheDef'), 'x');

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_AnsichtTab_K:DSID';

Insert into FX_ANSICHTTAB_K (ANSICHT,REIHENFOLGE,TABAKEY,TABELLE,JOIN1SPALTEAKEY,JOIN2TABAKEY,JOIN2SPALTEAKEY,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,ANSICHTDEFID,TABDEFID, JOINTYP) 
values ('FX_AnsichtDef_K','1','FX_AnsichtDef_K',null,'x','x','x','MW',to_date('02.07.18','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_AnsichtTab_K:DSID'),
(select a.dsid from FX_AnsichtDef_K a where a.ansicht = 'FX_AnsichtDef_K'),
(select a.dsid from FX_TabDef_K a where a.tabelle = 'FX_AnsichtDef_K'), 'x');

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_AnsichtTab_K:DSID';

Insert into FX_ANSICHTTAB_K (ANSICHT,REIHENFOLGE,TABAKEY,TABELLE,JOIN1SPALTEAKEY,JOIN2TABAKEY,JOIN2SPALTEAKEY,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,ANSICHTDEFID,TABDEFID, JOINTYP) 
values ('FX_AnsichtOrderBy_K','1','FX_AnsichtOrderBy_K',null,'x','x','x','MW',to_date('02.07.18','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_AnsichtTab_K:DSID'),
(select a.dsid from FX_AnsichtDef_K a where a.ansicht = 'FX_AnsichtOrderBy_K'),
(select a.dsid from FX_TabDef_K a where a.tabelle = 'FX_AnsichtOrderBy_K'), 'x');

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_AnsichtTab_K:DSID';

Insert into FX_ANSICHTTAB_K (ANSICHT,REIHENFOLGE,TABAKEY,TABELLE,JOIN1SPALTEAKEY,JOIN2TABAKEY,JOIN2SPALTEAKEY,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,ANSICHTDEFID,TABDEFID, JOINTYP) 
values ('FX_AnsichtSpalten_K','1','FX_AnsichtSpalten_K',null,'x','x','x','MW',to_date('02.07.18','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_AnsichtTab_K:DSID'),
(select a.dsid from FX_AnsichtDef_K a where a.ansicht = 'FX_AnsichtSpalten_K'),
(select a.dsid from FX_TabDef_K a where a.tabelle = 'FX_AnsichtSpalten_K'), 'x');

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_AnsichtTab_K:DSID';

Insert into FX_ANSICHTTAB_K (ANSICHT,REIHENFOLGE,TABAKEY,TABELLE,JOIN1SPALTEAKEY,JOIN2TABAKEY,JOIN2SPALTEAKEY,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,ANSICHTDEFID,TABDEFID, JOINTYP) 
values ('FX_AnsichtTab_K','1','FX_AnsichtTab_K',null,'x','x','x','MW',to_date('02.07.18','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_AnsichtTab_K:DSID'),
(select a.dsid from FX_AnsichtDef_K a where a.ansicht = 'FX_AnsichtTab_K'),
(select a.dsid from FX_TabDef_K a where a.tabelle = 'FX_AnsichtTab_K'), 'x');

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_AnsichtTab_K:DSID';

Insert into FX_ANSICHTTAB_K (ANSICHT,REIHENFOLGE,TABAKEY,TABELLE,JOIN1SPALTEAKEY,JOIN2TABAKEY,JOIN2SPALTEAKEY,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,ANSICHTDEFID,TABDEFID, JOINTYP) 
values ('FX_Menues_K','2','BenutzerBereicheDef',null,'BereichsId','FX_Menues_K','BereichsId','MW',to_date('02.07.18','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_AnsichtTab_K:DSID'),
(select a.dsid from FX_AnsichtDef_K a where a.ansicht = 'FX_Menues_K'),
(select a.dsid from FX_TabDef_K a where a.tabelle = 'BenutzerBereicheDef'), '=');

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_AnsichtTab_K:DSID';

Insert into FX_ANSICHTTAB_K (ANSICHT,REIHENFOLGE,TABAKEY,TABELLE,JOIN1SPALTEAKEY,JOIN2TABAKEY,JOIN2SPALTEAKEY,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,ANSICHTDEFID,TABDEFID, JOINTYP) 
values ('FX_Menues_K','3','FX_AnsichtDef_K',null,'DSID','FX_Menues_K','AnsichtDefId','MW',to_date('02.07.18','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_AnsichtTab_K:DSID'),
(select a.dsid from FX_AnsichtDef_K a where a.ansicht = 'FX_Menues_K'),
(select a.dsid from FX_TabDef_K a where a.tabelle = 'FX_AnsichtDef_K'), '=');

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_AnsichtTab_K:DSID';

Insert into FX_ANSICHTTAB_K (ANSICHT,REIHENFOLGE,TABAKEY,TABELLE,JOIN1SPALTEAKEY,JOIN2TABAKEY,JOIN2SPALTEAKEY,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,ANSICHTDEFID,TABDEFID, JOINTYP) 
values ('FX_Menues_K','1','FX_Menues_K',null,'x','x','x','MW',to_date('02.07.18','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_AnsichtTab_K:DSID'),
(select a.dsid from FX_AnsichtDef_K a where a.ansicht = 'FX_Menues_K'),
(select a.dsid from FX_TabDef_K a where a.tabelle = 'FX_Menues_K'), 'x');

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_AnsichtTab_K:DSID';

Insert into FX_ANSICHTTAB_K (ANSICHT,REIHENFOLGE,TABAKEY,TABELLE,JOIN1SPALTEAKEY,JOIN2TABAKEY,JOIN2SPALTEAKEY,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,ANSICHTDEFID,TABDEFID, JOINTYP) 
values ('FX_TabDef_K','2','BenutzerBereicheDef',null,'BereichsId','FX_TabDef_K','BereichsId','MW',to_date('02.07.18','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_AnsichtTab_K:DSID'),
(select a.dsid from FX_AnsichtDef_K a where a.ansicht = 'FX_TabDef_K'),
(select a.dsid from FX_TabDef_K a where a.tabelle = 'BenutzerBereicheDef'), '=');

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_AnsichtTab_K:DSID';

Insert into FX_ANSICHTTAB_K (ANSICHT,REIHENFOLGE,TABAKEY,TABELLE,JOIN1SPALTEAKEY,JOIN2TABAKEY,JOIN2SPALTEAKEY,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,ANSICHTDEFID,TABDEFID, JOINTYP) 
values ('FX_TabDef_K','1','FX_TabDef_K',null,'x','x','x','MR',to_date('02.07.18','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_AnsichtTab_K:DSID'),
(select a.dsid from FX_AnsichtDef_K a where a.ansicht = 'FX_TabDef_K'),
(select a.dsid from FX_TabDef_K a where a.tabelle = 'FX_TabDef_K'), 'x');

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_AnsichtTab_K:DSID';

Insert into FX_ANSICHTTAB_K (ANSICHT,REIHENFOLGE,TABAKEY,TABELLE,JOIN1SPALTEAKEY,JOIN2TABAKEY,JOIN2SPALTEAKEY,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,ANSICHTDEFID,TABDEFID, JOINTYP) 
values ('FX_TabSpEig_K','2','FX_TabDef_K',null,'DSID','FX_TabSpEig_K','TabDefId','MW',to_date('02.07.18','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_AnsichtTab_K:DSID'),
(select a.dsid from FX_AnsichtDef_K a where a.ansicht = 'FX_TabSpEig_K'),
(select a.dsid from FX_TabDef_K a where a.tabelle = 'FX_TabDef_K'), '=');

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_AnsichtTab_K:DSID';

Insert into FX_ANSICHTTAB_K (ANSICHT,REIHENFOLGE,TABAKEY,TABELLE,JOIN1SPALTEAKEY,JOIN2TABAKEY,JOIN2SPALTEAKEY,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,ANSICHTDEFID,TABDEFID, JOINTYP) 
values ('FX_TabSpEig_K','1','FX_TabSpEig_K',null,'x','x','x','MW',to_date('02.07.18','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_AnsichtTab_K:DSID'),
(select a.dsid from FX_AnsichtDef_K a where a.ansicht = 'FX_TabSpEig_K'),
(select a.dsid from FX_TabDef_K a where a.tabelle = 'FX_TabSpEig_K'), 'x');

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_AnsichtTab_K:DSID';

-- ***********************************
-- TabSpeig Datasets for FX_TabDef_K
-- ***********************************

Insert into FX_TABSPEIG_K (REIHENFOLGE,SPALTE,SPALTENKOPF,DBDATENTYP,PS,EINDEUTIG,EINGABENOTWENDIG,BEARBERLAUBT,SYSTEMWERT,DEFAULTWERT,MINIMUM,MAXIMUM,SPALTENTYP,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,TABDEFID) 
values ('2','Alias','Alias','STRING','0',null,'-1','-1','0',null,'3','255',null,'MW',to_date('03.07.18','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_TabSpEig_K:DSID'),
(select dsid from FX_TabDef_K where tabelle = 'FX_TabDef_K'));

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_TabSpEig_K:DSID';

Insert into FX_TABSPEIG_K (REIHENFOLGE,SPALTE,SPALTENKOPF,DBDATENTYP,PS,EINDEUTIG,EINGABENOTWENDIG,BEARBERLAUBT,SYSTEMWERT,DEFAULTWERT,MINIMUM,MAXIMUM,SPALTENTYP,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,TABDEFID) 
values ('25','BereichsId','Bereichs_ID','LONGINTEGER','0',null,'-1','-1','0',null,null,null,null,'MW',to_date('03.07.18','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_TabSpEig_K:DSID'),
(select dsid from FX_TabDef_K where tabelle = 'FX_TabDef_K'));

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_TabSpEig_K:DSID';

Insert into FX_TABSPEIG_K (REIHENFOLGE,SPALTE,SPALTENKOPF,DBDATENTYP,PS,EINDEUTIG,EINGABENOTWENDIG,BEARBERLAUBT,SYSTEMWERT,DEFAULTWERT,MINIMUM,MAXIMUM,SPALTENTYP,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,TABDEFID) 
values ('6','Bezeichnung','Bezeichnung','STRING','0',null,'0','-1','0',null,'0','255',null,'MW',to_date('03.07.18','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_TabSpEig_K:DSID'),
(select dsid from FX_TabDef_K where tabelle = 'FX_TabDef_K'));

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_TabSpEig_K:DSID';


Insert into FX_TABSPEIG_K (REIHENFOLGE,SPALTE,SPALTENKOPF,DBDATENTYP,PS,EINDEUTIG,EINGABENOTWENDIG,BEARBERLAUBT,SYSTEMWERT,DEFAULTWERT,MINIMUM,MAXIMUM,SPALTENTYP,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,TABDEFID) 
values ('20','DSID','DSID','LONGINTEGER','-1',null,'-1','-1','-1',null,null,null,'LaufendeNummer','MW',to_date('03.07.18','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_TabSpEig_K:DSID'),
(select dsid from FX_TabDef_K where tabelle = 'FX_TabDef_K'));

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_TabSpEig_K:DSID';

Insert into FX_TABSPEIG_K (REIHENFOLGE,SPALTE,SPALTENKOPF,DBDATENTYP,PS,EINDEUTIG,EINGABENOTWENDIG,BEARBERLAUBT,SYSTEMWERT,DEFAULTWERT,MINIMUM,MAXIMUM,SPALTENTYP,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,TABDEFID) 
values ('3','DatenBank','DatenBank','ENUM','0',null,'-1','-1','0',null,'0','255',null,'MW',to_date('03.07.18','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_TabSpEig_K:DSID'),
(select dsid from FX_TabDef_K where tabelle = 'FX_TabDef_K'));

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_TabSpEig_K:DSID';

Insert into FX_TABSPEIG_K (REIHENFOLGE,SPALTE,SPALTENKOPF,DBDATENTYP,PS,EINDEUTIG,EINGABENOTWENDIG,BEARBERLAUBT,SYSTEMWERT,DEFAULTWERT,MINIMUM,MAXIMUM,SPALTENTYP,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,TABDEFID) 
values ('13','EindeutigerSchluessel','EindeutigerSchluessel','STRING','0',null,'-1','-1','0',null,'0','255',null,'MW',to_date('03.07.18','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_TabSpEig_K:DSID'),
(select dsid from FX_TabDef_K where tabelle = 'FX_TabDef_K'));

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_TabSpEig_K:DSID';

Insert into FX_TABSPEIG_K (REIHENFOLGE,SPALTE,SPALTENKOPF,DBDATENTYP,PS,EINDEUTIG,EINGABENOTWENDIG,BEARBERLAUBT,SYSTEMWERT,DEFAULTWERT,MINIMUM,MAXIMUM,SPALTENTYP,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,TABDEFID) 
values ('5','Lizenz','Lizenz','STRING','0',null,'0','-1','0',null,'0','255',null,'MW',to_date('03.07.18','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_TabSpEig_K:DSID'),
(select dsid from FX_TabDef_K where tabelle = 'FX_TabDef_K'));

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_TabSpEig_K:DSID';

Insert into FX_TABSPEIG_K (REIHENFOLGE,SPALTE,SPALTENKOPF,DBDATENTYP,PS,EINDEUTIG,EINGABENOTWENDIG,BEARBERLAUBT,SYSTEMWERT,DEFAULTWERT,MINIMUM,MAXIMUM,SPALTENTYP,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,TABDEFID) 
values ('24','System','System','BOOLEAN','0',null,'-1','-1','0',null,null,null,null,'MW',to_date('03.07.18','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_TabSpEig_K:DSID'),
(select dsid from FX_TabDef_K where tabelle = 'FX_TabDef_K'));

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_TabSpEig_K:DSID';

Insert into FX_TABSPEIG_K (REIHENFOLGE,SPALTE,SPALTENKOPF,DBDATENTYP,PS,EINDEUTIG,EINGABENOTWENDIG,BEARBERLAUBT,SYSTEMWERT,DEFAULTWERT,MINIMUM,MAXIMUM,SPALTENTYP,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,TABDEFID) 
values ('1','Tabelle','Tabelle','STRING','0','10','-1','0','0',null,'3','255',null,'MW',to_date('03.07.18','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_TabSpEig_K:DSID'),
(select dsid from FX_TabDef_K where tabelle = 'FX_TabDef_K'));

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_TabSpEig_K:DSID';

Insert into FX_TABSPEIG_K (REIHENFOLGE,SPALTE,SPALTENKOPF,DBDATENTYP,PS,EINDEUTIG,EINGABENOTWENDIG,BEARBERLAUBT,SYSTEMWERT,DEFAULTWERT,MINIMUM,MAXIMUM,SPALTENTYP,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,TABDEFID) 
values ('10','ZeitTyp','ZeitTyp','ENUM','0',null,'0','-1','0',null,'0','255',null,'MW',to_date('03.07.18','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_TabSpEig_K:DSID'),
(select dsid from FX_TabDef_K where tabelle = 'FX_TabDef_K'));

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_TabSpEig_K:DSID';

Insert into FX_TABSPEIG_K (REIHENFOLGE,SPALTE,SPALTENKOPF,DBDATENTYP,PS,EINDEUTIG,EINGABENOTWENDIG,BEARBERLAUBT,SYSTEMWERT,DEFAULTWERT,MINIMUM,MAXIMUM,SPALTENTYP,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,TABDEFID) 
values ('22','angelegtAm','angelegt_am','DATETIME','0',null,'-1','-1','-1','#mwdata#now',null,null,'AngelegtAm','MW',to_date('03.07.18','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_TabSpEig_K:DSID'),
(select dsid from FX_TabDef_K where tabelle = 'FX_TabDef_K'));

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_TabSpEig_K:DSID';

Insert into FX_TABSPEIG_K (REIHENFOLGE,SPALTE,SPALTENKOPF,DBDATENTYP,PS,EINDEUTIG,EINGABENOTWENDIG,BEARBERLAUBT,SYSTEMWERT,DEFAULTWERT,MINIMUM,MAXIMUM,SPALTENTYP,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,TABDEFID) 
values ('21','angelegtVon','angelegt_von','STRING','0',null,'-1','-1','-1','#mwdata#userid','0','255','AngelegtVon','MW',to_date('03.07.18','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_TabSpEig_K:DSID'),
(select dsid from FX_TabDef_K where tabelle = 'FX_TabDef_K'));

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_TabSpEig_K:DSID';

Insert into FX_TABSPEIG_K (REIHENFOLGE,SPALTE,SPALTENKOPF,DBDATENTYP,PS,EINDEUTIG,EINGABENOTWENDIG,BEARBERLAUBT,SYSTEMWERT,DEFAULTWERT,MINIMUM,MAXIMUM,SPALTENTYP,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,TABDEFID) 
values ('26','fullClassName','fullClassName','STRING','0','-1','0','-1','0',null,null,null,null,'MW',to_date('03.07.18','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_TabSpEig_K:DSID'),
(select dsid from FX_TabDef_K where tabelle = 'FX_TabDef_K'));

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_TabSpEig_K:DSID';

-- insert Datasets for FX_TabSpEig_K for FX_TabSpEig_K
Insert into FX_TABSPEIG_K (REIHENFOLGE,SPALTE,SPALTENKOPF,DBDATENTYP,PS,EINDEUTIG,EINGABENOTWENDIG,BEARBERLAUBT,SYSTEMWERT,MINIMUM,MAXIMUM,SPALTENTYP,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,TABDEFID) 
values ('14','Maximum','Maximum','STRING','0',null,'0','-1','0','0','255',null,'MW',to_date('03.07.18','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_TabSpEig_K:DSID'),
(select dsid from FX_TabDef_K where tabelle = 'FX_TabSpEig_K'));

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_TabSpEig_K:DSID';

Insert into FX_TABSPEIG_K (REIHENFOLGE,SPALTE,SPALTENKOPF,DBDATENTYP,PS,EINDEUTIG,EINGABENOTWENDIG,BEARBERLAUBT,SYSTEMWERT,MINIMUM,MAXIMUM,SPALTENTYP,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,TABDEFID) 
values ('13','Minimum','Minimum','STRING','0',null,'0','-1','0','0','255',null,'MW',to_date('03.07.18','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_TabSpEig_K:DSID'),
(select dsid from FX_TabDef_K where tabelle = 'FX_TabSpEig_K'));

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_TabSpEig_K:DSID';

Insert into FX_TABSPEIG_K (REIHENFOLGE,SPALTE,SPALTENKOPF,DBDATENTYP,PS,EINDEUTIG,EINGABENOTWENDIG,BEARBERLAUBT,SYSTEMWERT,MINIMUM,MAXIMUM,SPALTENTYP,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,TABDEFID) 
values ('22','NichtEinlesen','NichtEinlesen','STRING','0',null,'0','-1','0','0','255',null,'MW',to_date('03.07.18','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_TabSpEig_K:DSID'),
(select dsid from FX_TabDef_K where tabelle = 'FX_TabSpEig_K'));

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_TabSpEig_K:DSID';

Insert into FX_TABSPEIG_K (REIHENFOLGE,SPALTE,SPALTENKOPF,DBDATENTYP,PS,EINDEUTIG,EINGABENOTWENDIG,BEARBERLAUBT,SYSTEMWERT,MINIMUM,MAXIMUM,SPALTENTYP,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,TABDEFID) 
values ('6','PS','PS','BOOLEAN','0',null,'0','-1','0',null,null,null,'MW',to_date('03.07.18','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_TabSpEig_K:DSID'),
(select dsid from FX_TabDef_K where tabelle = 'FX_TabSpEig_K'));

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_TabSpEig_K:DSID';

Insert into FX_TABSPEIG_K (REIHENFOLGE,SPALTE,SPALTENKOPF,DBDATENTYP,PS,EINDEUTIG,EINGABENOTWENDIG,BEARBERLAUBT,SYSTEMWERT,MINIMUM,MAXIMUM,SPALTENTYP,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,TABDEFID) 
values ('3','Spalte','Spalte','STRING','0','10','-1','-1','0','0','255',null,'MW',to_date('03.07.18','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_TabSpEig_K:DSID'),
(select dsid from FX_TabDef_K where tabelle = 'FX_TabSpEig_K'));

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_TabSpEig_K:DSID';

Insert into FX_TABSPEIG_K (REIHENFOLGE,SPALTE,SPALTENKOPF,DBDATENTYP,PS,EINDEUTIG,EINGABENOTWENDIG,BEARBERLAUBT,SYSTEMWERT,MINIMUM,MAXIMUM,SPALTENTYP,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,TABDEFID) 
values ('4','Spaltenkopf','Spaltenkopf','STRING','0',null,'-1','-1','0','0','255',null,'MW',to_date('03.07.18','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_TabSpEig_K:DSID'),
(select dsid from FX_TabDef_K where tabelle = 'FX_TabSpEig_K'));

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_TabSpEig_K:DSID';

Insert into FX_TABSPEIG_K (REIHENFOLGE,SPALTE,SPALTENKOPF,DBDATENTYP,PS,EINDEUTIG,EINGABENOTWENDIG,BEARBERLAUBT,SYSTEMWERT,MINIMUM,MAXIMUM,SPALTENTYP,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,TABDEFID) 
values ('2','Reihenfolge','Reihenfolge','LONGINTEGER','0',null,'-1','-1','0',null,null,null,'MW',to_date('03.07.18','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_TabSpEig_K:DSID'),
(select dsid from FX_TabDef_K where tabelle = 'FX_TabSpEig_K'));

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_TabSpEig_K:DSID';

Insert into FX_TABSPEIG_K (REIHENFOLGE,SPALTE,SPALTENKOPF,DBDATENTYP,PS,EINDEUTIG,EINGABENOTWENDIG,BEARBERLAUBT,SYSTEMWERT,MINIMUM,MAXIMUM,SPALTENTYP,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,TABDEFID) 
values ('28','TabDefId','TabDef_ID','LONGINTEGER','0',null,'-1','0','0',null,null,null,'MW',to_date('29.11.11','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_TabSpEig_K:DSID'),
(select dsid from FX_TabDef_K where tabelle = 'FX_TabSpEig_K'));

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_TabSpEig_K:DSID';

Insert into FX_TABSPEIG_K (REIHENFOLGE,SPALTE,SPALTENKOPF,DBDATENTYP,PS,EINDEUTIG,EINGABENOTWENDIG,BEARBERLAUBT,SYSTEMWERT,MINIMUM,MAXIMUM,SPALTENTYP,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,TABDEFID) 
values ('10','SystemWert','SystemWert','BOOLEAN','0',null,'0','-1','0',null,null,null,'MW',to_date('03.07.18','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_TabSpEig_K:DSID'),
(select dsid from FX_TabDef_K where tabelle = 'FX_TabSpEig_K'));

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_TabSpEig_K:DSID';

Insert into FX_TABSPEIG_K (REIHENFOLGE,SPALTE,SPALTENKOPF,DBDATENTYP,PS,EINDEUTIG,EINGABENOTWENDIG,BEARBERLAUBT,SYSTEMWERT,MINIMUM,MAXIMUM,SPALTENTYP,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,TABDEFID) 
values ('1','Tabelle','Tabelle','STRING','0',null,'-1','-1','0','0','255',null,'MW',to_date('03.07.18','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_TabSpEig_K:DSID'),
(select dsid from FX_TabDef_K where tabelle = 'FX_TabSpEig_K'));

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_TabSpEig_K:DSID';

Insert into FX_TABSPEIG_K (REIHENFOLGE,SPALTE,SPALTENKOPF,DBDATENTYP,PS,EINDEUTIG,EINGABENOTWENDIG,BEARBERLAUBT,SYSTEMWERT,MINIMUM,MAXIMUM,SPALTENTYP,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,TABDEFID) 
values ('8','EingabeNotwendig','EingabeNotwendig','BOOLEAN','0',null,'0','-1','0',null,null,null,'MW',to_date('03.07.18','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_TabSpEig_K:DSID'),
(select dsid from FX_TabDef_K where tabelle = 'FX_TabSpEig_K'));

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_TabSpEig_K:DSID';

Insert into FX_TABSPEIG_K (REIHENFOLGE,SPALTE,SPALTENKOPF,DBDATENTYP,PS,EINDEUTIG,EINGABENOTWENDIG,BEARBERLAUBT,SYSTEMWERT,MINIMUM,MAXIMUM,SPALTENTYP,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,TABDEFID) 
values ('20','FormatEingabe','FormatEingabe','STRING','0',null,'0','-1','0','0','255',null,'MW',to_date('03.07.18','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_TabSpEig_K:DSID'),
(select dsid from FX_TabDef_K where tabelle = 'FX_TabSpEig_K'));

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_TabSpEig_K:DSID';

Insert into FX_TABSPEIG_K (REIHENFOLGE,SPALTE,SPALTENKOPF,DBDATENTYP,PS,EINDEUTIG,EINGABENOTWENDIG,BEARBERLAUBT,SYSTEMWERT,MINIMUM,MAXIMUM,SPALTENTYP,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,TABDEFID) 
values ('27','System','System','BOOLEAN','0',null,'-1','-1','0',null,null,null,'MW',to_date('03.07.18','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_TabSpEig_K:DSID'),
(select dsid from FX_TabDef_K where tabelle = 'FX_TabSpEig_K'));

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_TabSpEig_K:DSID';

Insert into FX_TABSPEIG_K (REIHENFOLGE,SPALTE,SPALTENKOPF,DBDATENTYP,PS,EINDEUTIG,EINGABENOTWENDIG,BEARBERLAUBT,SYSTEMWERT,MINIMUM,MAXIMUM,SPALTENTYP,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,TABDEFID) 
values ('18','ToolTipText','ToolTipText','STRING','0',null,'0','-1','0','0','4000',null,'MW',to_date('03.07.18','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_TabSpEig_K:DSID'),
(select dsid from FX_TabDef_K where tabelle = 'FX_TabSpEig_K'));

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_TabSpEig_K:DSID';

Insert into FX_TABSPEIG_K (REIHENFOLGE,SPALTE,SPALTENKOPF,DBDATENTYP,PS,EINDEUTIG,EINGABENOTWENDIG,BEARBERLAUBT,SYSTEMWERT,MINIMUM,MAXIMUM,SPALTENTYP,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,TABDEFID) 
values ('12','TxtEig','TxtEig','STRING','0',null,'0','-1','0','0','255',null,'MW',to_date('03.07.18','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_TabSpEig_K:DSID'),
(select dsid from FX_TabDef_K where tabelle = 'FX_TabSpEig_K'));

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_TabSpEig_K:DSID';

Insert into FX_TABSPEIG_K (REIHENFOLGE,SPALTE,SPALTENKOPF,DBDATENTYP,PS,EINDEUTIG,EINGABENOTWENDIG,BEARBERLAUBT,SYSTEMWERT,MINIMUM,MAXIMUM,SPALTENTYP,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,TABDEFID) 
values ('17','SpaltenTyp','SpaltenTyp','STRING','0',null,'0','-1','0','0','255',null,'MW',to_date('03.07.18','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_TabSpEig_K:DSID'),
(select dsid from FX_TabDef_K where tabelle = 'FX_TabSpEig_K'));

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_TabSpEig_K:DSID';

Insert into FX_TABSPEIG_K (REIHENFOLGE,SPALTE,SPALTENKOPF,DBDATENTYP,PS,EINDEUTIG,EINGABENOTWENDIG,BEARBERLAUBT,SYSTEMWERT,MINIMUM,MAXIMUM,SPALTENTYP,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,TABDEFID) 
values ('15','Vergleichsoperator','Vergleichsoperator','STRING','0',null,'0','-1','0','0','255',null,'MW',to_date('03.07.18','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_TabSpEig_K:DSID'),
(select dsid from FX_TabDef_K where tabelle = 'FX_TabSpEig_K'));

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_TabSpEig_K:DSID';

Insert into FX_TABSPEIG_K (REIHENFOLGE,SPALTE,SPALTENKOPF,DBDATENTYP,PS,EINDEUTIG,EINGABENOTWENDIG,BEARBERLAUBT,SYSTEMWERT,MINIMUM,MAXIMUM,SPALTENTYP,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,TABDEFID) 
values ('16','VergleichsSpalte','VergleichsSpalte','STRING','0',null,'0','-1','0','0','255',null,'MW',to_date('03.07.18','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_TabSpEig_K:DSID'),
(select dsid from FX_TabDef_K where tabelle = 'FX_TabSpEig_K'));

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_TabSpEig_K:DSID';

Insert into FX_TABSPEIG_K (REIHENFOLGE,SPALTE,SPALTENKOPF,DBDATENTYP,PS,EINDEUTIG,EINGABENOTWENDIG,BEARBERLAUBT,SYSTEMWERT,MINIMUM,MAXIMUM,SPALTENTYP,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,TABDEFID) 
values ('19','Zeichenmenge','Zeichenmenge','STRING','0',null,'0','-1','0','0','255',null,'MW',to_date('03.07.18','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_TabSpEig_K:DSID'),
(select dsid from FX_TabDef_K where tabelle = 'FX_TabSpEig_K'));

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_TabSpEig_K:DSID';

Insert into FX_TABSPEIG_K (REIHENFOLGE,SPALTE,SPALTENKOPF,DBDATENTYP,PS,EINDEUTIG,EINGABENOTWENDIG,BEARBERLAUBT,SYSTEMWERT,MINIMUM,MAXIMUM,SPALTENTYP,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,TABDEFID) 
values ('21','AenderungNachVerriegelung','AenderungNachVerriegelung','LONGINTEGER','0',null,'0','-1','0',null,null,null,'MW',to_date('03.07.18','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_TabSpEig_K:DSID'),
(select dsid from FX_TabDef_K where tabelle = 'FX_TabSpEig_K'));

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_TabSpEig_K:DSID';

Insert into FX_TABSPEIG_K (REIHENFOLGE,SPALTE,SPALTENKOPF,DBDATENTYP,PS,EINDEUTIG,EINGABENOTWENDIG,BEARBERLAUBT,SYSTEMWERT,MINIMUM,MAXIMUM,SPALTENTYP,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,TABDEFID) 
values ('25','angelegtAm','angelegt_am','DATETIME','0',null,'-1','-1','-1',null,null,'AngelegtAm','MW',to_date('03.07.18','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_TabSpEig_K:DSID'),
(select dsid from FX_TabDef_K where tabelle = 'FX_TabSpEig_K'));

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_TabSpEig_K:DSID';

Insert into FX_TABSPEIG_K (REIHENFOLGE,SPALTE,SPALTENKOPF,DBDATENTYP,PS,EINDEUTIG,EINGABENOTWENDIG,BEARBERLAUBT,SYSTEMWERT,MINIMUM,MAXIMUM,SPALTENTYP,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,TABDEFID) 
values ('24','angelegtVon','angelegt_von','STRING','0',null,'0','-1','-1','0','255','AngelegtVon','MW',to_date('03.07.18','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_TabSpEig_K:DSID'),
(select dsid from FX_TabDef_K where tabelle = 'FX_TabSpEig_K'));

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_TabSpEig_K:DSID';

Insert into FX_TABSPEIG_K (REIHENFOLGE,SPALTE,SPALTENKOPF,DBDATENTYP,PS,EINDEUTIG,EINGABENOTWENDIG,BEARBERLAUBT,SYSTEMWERT,MINIMUM,MAXIMUM,SPALTENTYP,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,TABDEFID) 
values ('9','BearbErlaubt','BearbErlaubt','BOOLEAN','0',null,'0','-1','0',null,null,null,'MW',to_date('03.07.18','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_TabSpEig_K:DSID'),
(select dsid from FX_TabDef_K where tabelle = 'FX_TabSpEig_K'));

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_TabSpEig_K:DSID';

Insert into FX_TABSPEIG_K (REIHENFOLGE,SPALTE,SPALTENKOPF,DBDATENTYP,PS,EINDEUTIG,EINGABENOTWENDIG,BEARBERLAUBT,SYSTEMWERT,MINIMUM,MAXIMUM,SPALTENTYP,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,TABDEFID) 
values ('5','DBDatenTyp','DBDatenTyp','ENUM','0',null,'-1','-1','0','0','255',null,'MW',to_date('03.07.18','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_TabSpEig_K:DSID'),
(select dsid from FX_TabDef_K where tabelle = 'FX_TabSpEig_K'));

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_TabSpEig_K:DSID';

Insert into FX_TABSPEIG_K (REIHENFOLGE,SPALTE,SPALTENKOPF,DBDATENTYP,PS,EINDEUTIG,EINGABENOTWENDIG,BEARBERLAUBT,SYSTEMWERT,MINIMUM,MAXIMUM,SPALTENTYP,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,TABDEFID) 
values ('11','Defaultwert','Defaultwert','STRING','0',null,'0','-1','0','0','255',null,'MW',to_date('03.07.18','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_TabSpEig_K:DSID'),
(select dsid from FX_TabDef_K where tabelle = 'FX_TabSpEig_K'));

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_TabSpEig_K:DSID';

Insert into FX_TABSPEIG_K (REIHENFOLGE,SPALTE,SPALTENKOPF,DBDATENTYP,PS,EINDEUTIG,EINGABENOTWENDIG,BEARBERLAUBT,SYSTEMWERT,MINIMUM,MAXIMUM,SPALTENTYP,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,TABDEFID) 
values ('23','DSID','DSID','LONGINTEGER','-1',null,'-1','-1','-1',null,null,'LaufendeNummer','MW',to_date('03.07.18','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_TabSpEig_K:DSID'),
(select dsid from FX_TabDef_K where tabelle = 'FX_TabSpEig_K'));

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_TabSpEig_K:DSID';

Insert into FX_TABSPEIG_K (REIHENFOLGE,SPALTE,SPALTENKOPF,DBDATENTYP,PS,EINDEUTIG,EINGABENOTWENDIG,BEARBERLAUBT,SYSTEMWERT,MINIMUM,MAXIMUM,SPALTENTYP,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,TABDEFID) 
values ('7','Eindeutig','Eindeutig','LONGINTEGER','0',null,'0','-1','0',null,null,null,'MW',to_date('03.07.18','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_TabSpEig_K:DSID'),
(select dsid from FX_TabDef_K where tabelle = 'FX_TabSpEig_K'));

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_TabSpEig_K:DSID';

-- insert Datasets for FX_TabSpEig_K for FX_AnsichtDef_K
Insert into FX_TABSPEIG_K (REIHENFOLGE,SPALTE,SPALTENKOPF,DBDATENTYP,PS,EINDEUTIG,EINGABENOTWENDIG,BEARBERLAUBT,SYSTEMWERT,MINIMUM,MAXIMUM,SPALTENTYP,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,TABDEFID) 
values ('30','AngelegtAm','angelegt_am','DATETIME','0',null,'-1','-1','-1',null,null,'AngelegtAm','MW',to_date('03.07.18','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_TabSpEig_K:DSID'),
(select dsid from FX_TabDef_K where tabelle = 'FX_AnsichtDef_K'));

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_TabSpEig_K:DSID';

Insert into FX_TABSPEIG_K (REIHENFOLGE,SPALTE,SPALTENKOPF,DBDATENTYP,PS,EINDEUTIG,EINGABENOTWENDIG,BEARBERLAUBT,SYSTEMWERT,MINIMUM,MAXIMUM,SPALTENTYP,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,TABDEFID) 
values ('29','AngelegtVon','angelegt_von','STRING','0',null,'-1','-1','-1','0','255','AngelegtVon','MW',to_date('03.07.18','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_TabSpEig_K:DSID'),
(select dsid from FX_TabDef_K where tabelle = 'FX_AnsichtDef_K'));

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_TabSpEig_K:DSID';

Insert into FX_TABSPEIG_K (REIHENFOLGE,SPALTE,SPALTENKOPF,DBDATENTYP,PS,EINDEUTIG,EINGABENOTWENDIG,BEARBERLAUBT,SYSTEMWERT,MINIMUM,MAXIMUM,SPALTENTYP,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,TABDEFID) 
values ('4','Bezeichnung','Bezeichnung','STRING','0',null,'0','-1','0','0','255',null,'MW',to_date('03.07.18','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_TabSpEig_K:DSID'),
(select dsid from FX_TabDef_K where tabelle = 'FX_AnsichtDef_K'));

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_TabSpEig_K:DSID';

Insert into FX_TABSPEIG_K (REIHENFOLGE,SPALTE,SPALTENKOPF,DBDATENTYP,PS,EINDEUTIG,EINGABENOTWENDIG,BEARBERLAUBT,SYSTEMWERT,MINIMUM,MAXIMUM,SPALTENTYP,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,TABDEFID) 
values ('12','Importieren','Importieren','LONGINTEGER','0',null,'0','-1','0',null,null,null,'MW',to_date('03.07.18','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_TabSpEig_K:DSID'),
(select dsid from FX_TabDef_K where tabelle = 'FX_AnsichtDef_K'));

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_TabSpEig_K:DSID';

Insert into FX_TABSPEIG_K (REIHENFOLGE,SPALTE,SPALTENKOPF,DBDATENTYP,PS,EINDEUTIG,EINGABENOTWENDIG,BEARBERLAUBT,SYSTEMWERT,MINIMUM,MAXIMUM,SPALTENTYP,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,TABDEFID) 
values ('9','Bearbeiten','Bearbeiten','LONGINTEGER','0',null,'0','-1','0',null,null,null,'MW',to_date('03.07.18','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_TabSpEig_K:DSID'),
(select dsid from FX_TabDef_K where tabelle = 'FX_AnsichtDef_K'));

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_TabSpEig_K:DSID';

Insert into FX_TABSPEIG_K (REIHENFOLGE,SPALTE,SPALTENKOPF,DBDATENTYP,PS,EINDEUTIG,EINGABENOTWENDIG,BEARBERLAUBT,SYSTEMWERT,MINIMUM,MAXIMUM,SPALTENTYP,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,TABDEFID) 
values ('28','DSID','DSID','LONGINTEGER','-1',null,'-1','-1','-1',null,null,'LaufendeNummer','MW',to_date('03.07.18','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_TabSpEig_K:DSID'),
(select dsid from FX_TabDef_K where tabelle = 'FX_AnsichtDef_K'));

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_TabSpEig_K:DSID';

Insert into FX_TABSPEIG_K (REIHENFOLGE,SPALTE,SPALTENKOPF,DBDATENTYP,PS,EINDEUTIG,EINGABENOTWENDIG,BEARBERLAUBT,SYSTEMWERT,MINIMUM,MAXIMUM,SPALTENTYP,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,TABDEFID) 
values ('10','Entfernen','Entfernen','LONGINTEGER','0',null,'0','-1','0',null,null,null,'MW',to_date('03.07.18','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_TabSpEig_K:DSID'),
(select dsid from FX_TabDef_K where tabelle = 'FX_AnsichtDef_K'));

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_TabSpEig_K:DSID';

Insert into FX_TABSPEIG_K (REIHENFOLGE,SPALTE,SPALTENKOPF,DBDATENTYP,PS,EINDEUTIG,EINGABENOTWENDIG,BEARBERLAUBT,SYSTEMWERT,MINIMUM,MAXIMUM,SPALTENTYP,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,TABDEFID) 
values ('33','urlPath','UrlPath','STRING','0',null,'-1','-1','0',null,null,null,'MW',to_date('08.03.12','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_TabSpEig_K:DSID'),
(select dsid from FX_TabDef_K where tabelle = 'FX_AnsichtDef_K'));

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_TabSpEig_K:DSID';

Insert into FX_TABSPEIG_K (REIHENFOLGE,SPALTE,SPALTENKOPF,DBDATENTYP,PS,EINDEUTIG,EINGABENOTWENDIG,BEARBERLAUBT,SYSTEMWERT,MINIMUM,MAXIMUM,SPALTENTYP,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,TABDEFID) 
values ('15','Filter','Filter','LONGINTEGER','0',null,'0','-1','0',null,null,null,'MW',to_date('03.07.18','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_TabSpEig_K:DSID'),
(select dsid from FX_TabDef_K where tabelle = 'FX_AnsichtDef_K'));

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_TabSpEig_K:DSID';

Insert into FX_TABSPEIG_K (REIHENFOLGE,SPALTE,SPALTENKOPF,DBDATENTYP,PS,EINDEUTIG,EINGABENOTWENDIG,BEARBERLAUBT,SYSTEMWERT,MINIMUM,MAXIMUM,SPALTENTYP,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,TABDEFID) 
values ('7','Hinzufuegen','Hinzufuegen','LONGINTEGER','0',null,'0','-1','0',null,null,null,'MW',to_date('03.07.18','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_TabSpEig_K:DSID'),
(select dsid from FX_TabDef_K where tabelle = 'FX_AnsichtDef_K'));

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_TabSpEig_K:DSID';

Insert into FX_TABSPEIG_K (REIHENFOLGE,SPALTE,SPALTENKOPF,DBDATENTYP,PS,EINDEUTIG,EINGABENOTWENDIG,BEARBERLAUBT,SYSTEMWERT,MINIMUM,MAXIMUM,SPALTENTYP,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,TABDEFID) 
values ('14','Lesen','Lesen','LONGINTEGER','0',null,'0','-1','0',null,null,null,'MW',to_date('03.07.18','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_TabSpEig_K:DSID'),
(select dsid from FX_TabDef_K where tabelle = 'FX_AnsichtDef_K'));

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_TabSpEig_K:DSID';

Insert into FX_TABSPEIG_K (REIHENFOLGE,SPALTE,SPALTENKOPF,DBDATENTYP,PS,EINDEUTIG,EINGABENOTWENDIG,BEARBERLAUBT,SYSTEMWERT,MINIMUM,MAXIMUM,SPALTENTYP,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,TABDEFID) 
values ('1','Ansicht','Ansicht','STRING','0','10','-1','-1','0','0','255',null,'MW',to_date('03.07.18','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_TabSpEig_K:DSID'),
(select dsid from FX_TabDef_K where tabelle = 'FX_AnsichtDef_K'));

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_TabSpEig_K:DSID';

Insert into FX_TABSPEIG_K (REIHENFOLGE,SPALTE,SPALTENKOPF,DBDATENTYP,PS,EINDEUTIG,EINGABENOTWENDIG,BEARBERLAUBT,SYSTEMWERT,MINIMUM,MAXIMUM,SPALTENTYP,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,TABDEFID) 
values ('16','Sortieren','Sortieren','LONGINTEGER','0',null,'0','-1','0',null,null,null,'MW',to_date('03.07.18','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_TabSpEig_K:DSID'),
(select dsid from FX_TabDef_K where tabelle = 'FX_AnsichtDef_K'));

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_TabSpEig_K:DSID';

Insert into FX_TABSPEIG_K (REIHENFOLGE,SPALTE,SPALTENKOPF,DBDATENTYP,PS,EINDEUTIG,EINGABENOTWENDIG,BEARBERLAUBT,SYSTEMWERT,MINIMUM,MAXIMUM,SPALTENTYP,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,TABDEFID) 
values ('32','System','System','BOOLEAN','0',null,'-1','-1','0',null,null,null,'MW',to_date('03.07.18','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_TabSpEig_K:DSID'),
(select dsid from FX_TabDef_K where tabelle = 'FX_AnsichtDef_K'));

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_TabSpEig_K:DSID';

-- insert Datasets for FX_TabSpEig_K for FX_AnsichtTab_K
Insert into FX_TABSPEIG_K (REIHENFOLGE,SPALTE,SPALTENKOPF,DBDATENTYP,PS,EINDEUTIG,EINGABENOTWENDIG,BEARBERLAUBT,SYSTEMWERT,MINIMUM,MAXIMUM,SPALTENTYP,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,TABDEFID) 
values ('14','DSID','DSID','LONGINTEGER','-1',null,'-1','-1','-1',null,null,'LaufendeNummer','MW',to_date('03.07.18','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_TabSpEig_K:DSID'),
(select dsid from FX_TabDef_K where tabelle = 'FX_AnsichtTab_K'));

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_TabSpEig_K:DSID';

Insert into FX_TABSPEIG_K (REIHENFOLGE,SPALTE,SPALTENKOPF,DBDATENTYP,PS,EINDEUTIG,EINGABENOTWENDIG,BEARBERLAUBT,SYSTEMWERT,MINIMUM,MAXIMUM,SPALTENTYP,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,TABDEFID) 
values ('11','Join1SpalteAKey','Join1SpalteAKey','STRING','0',null,'-1','-1','0','0','255',null,'MW',to_date('03.07.18','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_TabSpEig_K:DSID'),
(select dsid from FX_TabDef_K where tabelle = 'FX_AnsichtTab_K'));

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_TabSpEig_K:DSID';

Insert into FX_TABSPEIG_K (REIHENFOLGE,SPALTE,SPALTENKOPF,DBDATENTYP,PS,EINDEUTIG,EINGABENOTWENDIG,BEARBERLAUBT,SYSTEMWERT,MINIMUM,MAXIMUM,SPALTENTYP,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,TABDEFID) 
values ('13','Join2SpalteAKey','Join2SpalteAKey','STRING','0',null,'-1','-1','0','0','255',null,'MW',to_date('03.07.18','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_TabSpEig_K:DSID'),
(select dsid from FX_TabDef_K where tabelle = 'FX_AnsichtTab_K'));

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_TabSpEig_K:DSID';

Insert into FX_TABSPEIG_K (REIHENFOLGE,SPALTE,SPALTENKOPF,DBDATENTYP,PS,EINDEUTIG,EINGABENOTWENDIG,BEARBERLAUBT,SYSTEMWERT,MINIMUM,MAXIMUM,SPALTENTYP,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,TABDEFID) 
values ('10','JoinTyp','JoinTyp','STRING','0',null,'-1','-1','0','0','255',null,'MW',to_date('03.07.18','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_TabSpEig_K:DSID'),
(select dsid from FX_TabDef_K where tabelle = 'FX_AnsichtTab_K'));

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_TabSpEig_K:DSID';

Insert into FX_TABSPEIG_K (REIHENFOLGE,SPALTE,SPALTENKOPF,DBDATENTYP,PS,EINDEUTIG,EINGABENOTWENDIG,BEARBERLAUBT,SYSTEMWERT,MINIMUM,MAXIMUM,SPALTENTYP,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,TABDEFID) 
values ('12','Join2TabAKey','Join2TabAKey','STRING','0',null,'-1','-1','0','0','255',null,'MW',to_date('03.07.18','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_TabSpEig_K:DSID'),
(select dsid from FX_TabDef_K where tabelle = 'FX_AnsichtTab_K'));

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_TabSpEig_K:DSID';

Insert into FX_TABSPEIG_K (REIHENFOLGE,SPALTE,SPALTENKOPF,DBDATENTYP,PS,EINDEUTIG,EINGABENOTWENDIG,BEARBERLAUBT,SYSTEMWERT,MINIMUM,MAXIMUM,SPALTENTYP,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,TABDEFID) 
values ('16','angelegtAm','angelegt_am','DATETIME','0',null,'-1','-1','-1',null,null,'AngelegtAm','MW',to_date('03.07.18','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_TabSpEig_K:DSID'),
(select dsid from FX_TabDef_K where tabelle = 'FX_AnsichtTab_K'));

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_TabSpEig_K:DSID';

Insert into FX_TABSPEIG_K (REIHENFOLGE,SPALTE,SPALTENKOPF,DBDATENTYP,PS,EINDEUTIG,EINGABENOTWENDIG,BEARBERLAUBT,SYSTEMWERT,MINIMUM,MAXIMUM,SPALTENTYP,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,TABDEFID) 
values ('1','Ansicht','Ansicht','STRING','0','10','-1','-1','0','0','255',null,'MW',to_date('03.07.18','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_TabSpEig_K:DSID'),
(select dsid from FX_TabDef_K where tabelle = 'FX_AnsichtTab_K'));

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_TabSpEig_K:DSID';

Insert into FX_TABSPEIG_K (REIHENFOLGE,SPALTE,SPALTENKOPF,DBDATENTYP,PS,EINDEUTIG,EINGABENOTWENDIG,BEARBERLAUBT,SYSTEMWERT,MINIMUM,MAXIMUM,SPALTENTYP,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,TABDEFID) 
values ('7','BearbeitenZugelassen','BearbeitenZugelassen','LONGINTEGER','0',null,'0','-1','0',null,null,null,'MW',to_date('03.07.18','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_TabSpEig_K:DSID'),
(select dsid from FX_TabDef_K where tabelle = 'FX_AnsichtTab_K'));

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_TabSpEig_K:DSID';

Insert into FX_TABSPEIG_K (REIHENFOLGE,SPALTE,SPALTENKOPF,DBDATENTYP,PS,EINDEUTIG,EINGABENOTWENDIG,BEARBERLAUBT,SYSTEMWERT,MINIMUM,MAXIMUM,SPALTENTYP,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,TABDEFID) 
values ('2','Reihenfolge','Reihenfolge','LONGINTEGER','0',null,'-1','-1','0',null,null,null,'MW',to_date('03.07.18','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_TabSpEig_K:DSID'),
(select dsid from FX_TabDef_K where tabelle = 'FX_AnsichtTab_K'));

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_TabSpEig_K:DSID';

Insert into FX_TABSPEIG_K (REIHENFOLGE,SPALTE,SPALTENKOPF,DBDATENTYP,PS,EINDEUTIG,EINGABENOTWENDIG,BEARBERLAUBT,SYSTEMWERT,MINIMUM,MAXIMUM,SPALTENTYP,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,TABDEFID) 
values ('6','Alias','Alias','STRING','0',null,'0','-1','0','0','255',null,'MW',to_date('03.07.18','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_TabSpEig_K:DSID'),
(select dsid from FX_TabDef_K where tabelle = 'FX_AnsichtTab_K'));

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_TabSpEig_K:DSID';

Insert into FX_TABSPEIG_K (REIHENFOLGE,SPALTE,SPALTENKOPF,DBDATENTYP,PS,EINDEUTIG,EINGABENOTWENDIG,BEARBERLAUBT,SYSTEMWERT,MINIMUM,MAXIMUM,SPALTENTYP,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,TABDEFID) 
values ('18','System','System','BOOLEAN','0',null,'-1','-1','0',null,null,null,'MW',to_date('03.07.18','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_TabSpEig_K:DSID'),
(select dsid from FX_TabDef_K where tabelle = 'FX_AnsichtTab_K'));

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_TabSpEig_K:DSID';

Insert into FX_TABSPEIG_K (REIHENFOLGE,SPALTE,SPALTENKOPF,DBDATENTYP,PS,EINDEUTIG,EINGABENOTWENDIG,BEARBERLAUBT,SYSTEMWERT,MINIMUM,MAXIMUM,SPALTENTYP,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,TABDEFID) 
values ('15','angelegtVon','angelegt_von','STRING','0',null,'-1','-1','-1','0','255','AngelegtVon','MW',to_date('03.07.18','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_TabSpEig_K:DSID'),
(select dsid from FX_TabDef_K where tabelle = 'FX_AnsichtTab_K'));

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_TabSpEig_K:DSID';

Insert into FX_TABSPEIG_K (REIHENFOLGE,SPALTE,SPALTENKOPF,DBDATENTYP,PS,EINDEUTIG,EINGABENOTWENDIG,BEARBERLAUBT,SYSTEMWERT,MINIMUM,MAXIMUM,SPALTENTYP,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,TABDEFID) 
values ('3','TabAkey','TabAkey','STRING','0','10','-1','-1','0','0','255',null,'MW',to_date('03.07.18','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_TabSpEig_K:DSID'),
(select dsid from FX_TabDef_K where tabelle = 'FX_AnsichtTab_K'));

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_TabSpEig_K:DSID';

Insert into FX_TABSPEIG_K (REIHENFOLGE,SPALTE,SPALTENKOPF,DBDATENTYP,PS,EINDEUTIG,EINGABENOTWENDIG,BEARBERLAUBT,SYSTEMWERT,MINIMUM,MAXIMUM,SPALTENTYP,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,TABDEFID) 
values ('4','Tabelle','Tabelle','STRING','0',null,'0','-1','0','0','255',null,'MW',to_date('03.07.18','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_TabSpEig_K:DSID'),
(select dsid from FX_TabDef_K where tabelle = 'FX_AnsichtTab_K'));

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_TabSpEig_K:DSID';

-- insert Datasets for FX_TabSpEig_K for FX_AnsichtOrderBy_K
Insert into FX_TABSPEIG_K (REIHENFOLGE,SPALTE,SPALTENKOPF,DBDATENTYP,PS,EINDEUTIG,EINGABENOTWENDIG,BEARBERLAUBT,SYSTEMWERT,MINIMUM,MAXIMUM,SPALTENTYP,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,TABDEFID) 
values ('8','angelegtAm','angelegt_am','DATETIME','0',null,'-1','-1','-1',null,null,'AngelegtAm','MW',to_date('03.07.18','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_TabSpEig_K:DSID'),
(select dsid from FX_TabDef_K where tabelle = 'FX_AnsichtOrderBy_K'));

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_TabSpEig_K:DSID';

Insert into FX_TABSPEIG_K (REIHENFOLGE,SPALTE,SPALTENKOPF,DBDATENTYP,PS,EINDEUTIG,EINGABENOTWENDIG,BEARBERLAUBT,SYSTEMWERT,MINIMUM,MAXIMUM,SPALTENTYP,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,TABDEFID) 
values ('1','Ansicht','Ansicht','STRING','0','10','-1','-1','0','0','255',null,'MW',to_date('03.07.18','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_TabSpEig_K:DSID'),
(select dsid from FX_TabDef_K where tabelle = 'FX_AnsichtOrderBy_K'));

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_TabSpEig_K:DSID';

Insert into FX_TABSPEIG_K (REIHENFOLGE,SPALTE,SPALTENKOPF,DBDATENTYP,PS,EINDEUTIG,EINGABENOTWENDIG,BEARBERLAUBT,SYSTEMWERT,MINIMUM,MAXIMUM,SPALTENTYP,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,TABDEFID) 
values ('5','Aufsteigend','Aufsteigend','LONGINTEGER','0',null,'0','-1','0',null,null,null,'MW',to_date('03.07.18','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_TabSpEig_K:DSID'),
(select dsid from FX_TabDef_K where tabelle = 'FX_AnsichtOrderBy_K'));

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_TabSpEig_K:DSID';

Insert into FX_TABSPEIG_K (REIHENFOLGE,SPALTE,SPALTENKOPF,DBDATENTYP,PS,EINDEUTIG,EINGABENOTWENDIG,BEARBERLAUBT,SYSTEMWERT,MINIMUM,MAXIMUM,SPALTENTYP,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,TABDEFID) 
values ('6','DSID','DSID','LONGINTEGER','-1',null,'-1','-1','-1',null,null,'LaufendeNummer','MW',to_date('03.07.18','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_TabSpEig_K:DSID'),
(select dsid from FX_TabDef_K where tabelle = 'FX_AnsichtOrderBy_K'));

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_TabSpEig_K:DSID';

Insert into FX_TABSPEIG_K (REIHENFOLGE,SPALTE,SPALTENKOPF,DBDATENTYP,PS,EINDEUTIG,EINGABENOTWENDIG,BEARBERLAUBT,SYSTEMWERT,MINIMUM,MAXIMUM,SPALTENTYP,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,TABDEFID) 
values ('2','Reihenfolge','Reihenfolge','LONGINTEGER','0',null,'-1','-1','0',null,null,null,'MW',to_date('03.07.18','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_TabSpEig_K:DSID'),
(select dsid from FX_TabDef_K where tabelle = 'FX_AnsichtOrderBy_K'));

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_TabSpEig_K:DSID';

Insert into FX_TABSPEIG_K (REIHENFOLGE,SPALTE,SPALTENKOPF,DBDATENTYP,PS,EINDEUTIG,EINGABENOTWENDIG,BEARBERLAUBT,SYSTEMWERT,MINIMUM,MAXIMUM,SPALTENTYP,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,TABDEFID) 
values ('4','SpalteAKey','SpalteAKey','STRING','0','10','-1','-1','0','0','255',null,'MW',to_date('03.07.18','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_TabSpEig_K:DSID'),
(select dsid from FX_TabDef_K where tabelle = 'FX_AnsichtOrderBy_K'));

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_TabSpEig_K:DSID';

Insert into FX_TABSPEIG_K (REIHENFOLGE,SPALTE,SPALTENKOPF,DBDATENTYP,PS,EINDEUTIG,EINGABENOTWENDIG,BEARBERLAUBT,SYSTEMWERT,MINIMUM,MAXIMUM,SPALTENTYP,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,TABDEFID) 
values ('3','TabAKey','TabAKey','STRING','0','10','-1','-1','0','0','255',null,'MW',to_date('03.07.18','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_TabSpEig_K:DSID'),
(select dsid from FX_TabDef_K where tabelle = 'FX_AnsichtOrderBy_K'));

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_TabSpEig_K:DSID';

Insert into FX_TABSPEIG_K (REIHENFOLGE,SPALTE,SPALTENKOPF,DBDATENTYP,PS,EINDEUTIG,EINGABENOTWENDIG,BEARBERLAUBT,SYSTEMWERT,MINIMUM,MAXIMUM,SPALTENTYP,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,TABDEFID) 
values ('10','System','System','BOOLEAN','0',null,'-1','-1','0',null,null,null,'MW',to_date('03.07.18','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_TabSpEig_K:DSID'),
(select dsid from FX_TabDef_K where tabelle = 'FX_AnsichtOrderBy_K'));

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_TabSpEig_K:DSID';

Insert into FX_TABSPEIG_K (REIHENFOLGE,SPALTE,SPALTENKOPF,DBDATENTYP,PS,EINDEUTIG,EINGABENOTWENDIG,BEARBERLAUBT,SYSTEMWERT,MINIMUM,MAXIMUM,SPALTENTYP,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,TABDEFID) 
values ('7','angelegtVon','angelegt_von','STRING','0',null,'-1','-1','-1','0','255','AngelegtVon','MW',to_date('03.07.18','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_TabSpEig_K:DSID'),
(select dsid from FX_TabDef_K where tabelle = 'FX_AnsichtOrderBy_K'));

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_TabSpEig_K:DSID';

-- insert Datasets for FX_TabSpEig_K for FX_AnsichtSpalten_K
Insert into FX_TABSPEIG_K (REIHENFOLGE,SPALTE,SPALTENKOPF,DBDATENTYP,PS,EINDEUTIG,EINGABENOTWENDIG,BEARBERLAUBT,SYSTEMWERT,MINIMUM,MAXIMUM,SPALTENTYP,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,TABDEFID) 
values ('42','angelegtAm','angelegt_am','DATETIME','0',null,'-1','-1','-1',null,null,'AngelegtAm','MW',to_date('03.07.18','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_TabSpEig_K:DSID'),
(select dsid from FX_TabDef_K where tabelle = 'FX_AnsichtSpalten_K'));

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_TabSpEig_K:DSID';

Insert into FX_TABSPEIG_K (REIHENFOLGE,SPALTE,SPALTENKOPF,DBDATENTYP,PS,EINDEUTIG,EINGABENOTWENDIG,BEARBERLAUBT,SYSTEMWERT,MINIMUM,MAXIMUM,SPALTENTYP,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,TABDEFID) 
values ('7','InGridAnzeigen','InGridAnzeigen','LONGINTEGER','0',null,'0','-1','0',null,null,null,'MW',to_date('03.07.18','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_TabSpEig_K:DSID'),
(select dsid from FX_TabDef_K where tabelle = 'FX_AnsichtSpalten_K'));

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_TabSpEig_K:DSID';

Insert into FX_TABSPEIG_K (REIHENFOLGE,SPALTE,SPALTENKOPF,DBDATENTYP,PS,EINDEUTIG,EINGABENOTWENDIG,BEARBERLAUBT,SYSTEMWERT,MINIMUM,MAXIMUM,SPALTENTYP,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,TABDEFID) 
values ('41','AngelegtVon','angelegt_von','STRING','0',null,'-1','-1','-1','0','255','AngelegtVon','MW',to_date('03.07.18','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_TabSpEig_K:DSID'),
(select dsid from FX_TabDef_K where tabelle = 'FX_AnsichtSpalten_K'));

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_TabSpEig_K:DSID';

Insert into FX_TABSPEIG_K (REIHENFOLGE,SPALTE,SPALTENKOPF,DBDATENTYP,PS,EINDEUTIG,EINGABENOTWENDIG,BEARBERLAUBT,SYSTEMWERT,MINIMUM,MAXIMUM,SPALTENTYP,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,TABDEFID) 
values ('15','BearbHinZufZugelassen','BearbHinZufZugelassen','LONGINTEGER','0',null,'0','-1','0',null,null,null,'MW',to_date('03.07.18','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_TabSpEig_K:DSID'),
(select dsid from FX_TabDef_K where tabelle = 'FX_AnsichtSpalten_K'));

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_TabSpEig_K:DSID';

Insert into FX_TABSPEIG_K (REIHENFOLGE,SPALTE,SPALTENKOPF,DBDATENTYP,PS,EINDEUTIG,EINGABENOTWENDIG,BEARBERLAUBT,SYSTEMWERT,MINIMUM,MAXIMUM,SPALTENTYP,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,TABDEFID) 
values ('16','BearbZugelassen','BearbZugelassen','LONGINTEGER','0',null,'0','-1','0',null,null,null,'MW',to_date('03.07.18','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_TabSpEig_K:DSID'),
(select dsid from FX_TabDef_K where tabelle = 'FX_AnsichtSpalten_K'));

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_TabSpEig_K:DSID';

Insert into FX_TABSPEIG_K (REIHENFOLGE,SPALTE,SPALTENKOPF,DBDATENTYP,PS,EINDEUTIG,EINGABENOTWENDIG,BEARBERLAUBT,SYSTEMWERT,MINIMUM,MAXIMUM,SPALTENTYP,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,TABDEFID) 
values ('40','DSID','DSID','LONGINTEGER','-1',null,'-1','-1','-1',null,null,'LaufendeNummer','MW',to_date('03.07.18','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_TabSpEig_K:DSID'),
(select dsid from FX_TabDef_K where tabelle = 'FX_AnsichtSpalten_K'));

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_TabSpEig_K:DSID';

Insert into FX_TABSPEIG_K (REIHENFOLGE,SPALTE,SPALTENKOPF,DBDATENTYP,PS,EINDEUTIG,EINGABENOTWENDIG,BEARBERLAUBT,SYSTEMWERT,MINIMUM,MAXIMUM,SPALTENTYP,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,TABDEFID) 
values ('17','EingabeNotwendig','EingabeNotwendig','LONGINTEGER','0',null,'0','-1','0',null,null,null,'MW',to_date('03.07.18','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_TabSpEig_K:DSID'),
(select dsid from FX_TabDef_K where tabelle = 'FX_AnsichtSpalten_K'));

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_TabSpEig_K:DSID';

Insert into FX_TABSPEIG_K (REIHENFOLGE,SPALTE,SPALTENKOPF,DBDATENTYP,PS,EINDEUTIG,EINGABENOTWENDIG,BEARBERLAUBT,SYSTEMWERT,MINIMUM,MAXIMUM,SPALTENTYP,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,TABDEFID) 
values ('2','IndexGrid','IndexGrid','LONGINTEGER','0',null,'0','-1','0',null,null,null,'MW',to_date('03.07.18','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_TabSpEig_K:DSID'),
(select dsid from FX_TabDef_K where tabelle = 'FX_AnsichtSpalten_K'));

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_TabSpEig_K:DSID';

Insert into FX_TABSPEIG_K (REIHENFOLGE,SPALTE,SPALTENKOPF,DBDATENTYP,PS,EINDEUTIG,EINGABENOTWENDIG,BEARBERLAUBT,SYSTEMWERT,MINIMUM,MAXIMUM,SPALTENTYP,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,TABDEFID) 
values ('8','InGridLaden','InGridLaden','LONGINTEGER','0',null,'0','-1','0',null,null,null,'MW',to_date('03.07.18','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_TabSpEig_K:DSID'),
(select dsid from FX_TabDef_K where tabelle = 'FX_AnsichtSpalten_K'));

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_TabSpEig_K:DSID';

Insert into FX_TABSPEIG_K (REIHENFOLGE,SPALTE,SPALTENKOPF,DBDATENTYP,PS,EINDEUTIG,EINGABENOTWENDIG,BEARBERLAUBT,SYSTEMWERT,MINIMUM,MAXIMUM,SPALTENTYP,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,TABDEFID) 
values ('4','SpalteAKey','SpalteAKey','STRING','0','10','-1','-1','0','0','255',null,'MW',to_date('03.07.18','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_TabSpEig_K:DSID'),
(select dsid from FX_TabDef_K where tabelle = 'FX_AnsichtSpalten_K'));

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_TabSpEig_K:DSID';

Insert into FX_TABSPEIG_K (REIHENFOLGE,SPALTE,SPALTENKOPF,DBDATENTYP,PS,EINDEUTIG,EINGABENOTWENDIG,BEARBERLAUBT,SYSTEMWERT,MINIMUM,MAXIMUM,SPALTENTYP,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,TABDEFID) 
values ('46','System','System','BOOLEAN','0',null,'-1','-1','0',null,null,null,'MW',to_date('03.07.18','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_TabSpEig_K:DSID'),
(select dsid from FX_TabDef_K where tabelle = 'FX_AnsichtSpalten_K'));

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_TabSpEig_K:DSID';

Insert into FX_TABSPEIG_K (REIHENFOLGE,SPALTE,SPALTENKOPF,DBDATENTYP,PS,EINDEUTIG,EINGABENOTWENDIG,BEARBERLAUBT,SYSTEMWERT,MINIMUM,MAXIMUM,SPALTENTYP,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,TABDEFID) 
values ('24','SuchWertAusSpalteAKey','SuchWertAusSpalteAKey','STRING','0',null,'0','-1','0','0','255',null,'MW',to_date('03.07.18','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_TabSpEig_K:DSID'),
(select dsid from FX_TabDef_K where tabelle = 'FX_AnsichtSpalten_K'));

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_TabSpEig_K:DSID';

Insert into FX_TABSPEIG_K (REIHENFOLGE,SPALTE,SPALTENKOPF,DBDATENTYP,PS,EINDEUTIG,EINGABENOTWENDIG,BEARBERLAUBT,SYSTEMWERT,MINIMUM,MAXIMUM,SPALTENTYP,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,TABDEFID) 
values ('23','SuchWertAusTabAKey','SuchWertAusTabAKey','STRING','0',null,'0','-1','0','0','255',null,'MW',to_date('03.07.18','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_TabSpEig_K:DSID'),
(select dsid from FX_TabDef_K where tabelle = 'FX_AnsichtSpalten_K'));

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_TabSpEig_K:DSID';

Insert into FX_TABSPEIG_K (REIHENFOLGE,SPALTE,SPALTENKOPF,DBDATENTYP,PS,EINDEUTIG,EINGABENOTWENDIG,BEARBERLAUBT,SYSTEMWERT,MINIMUM,MAXIMUM,SPALTENTYP,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,TABDEFID) 
values ('3','TabAKey','TabAKey','STRING','0','10','-1','-1','0','0','255',null,'MW',to_date('03.07.18','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_TabSpEig_K:DSID'),
(select dsid from FX_TabDef_K where tabelle = 'FX_AnsichtSpalten_K'));

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_TabSpEig_K:DSID';

Insert into FX_TABSPEIG_K (REIHENFOLGE,SPALTE,SPALTENKOPF,DBDATENTYP,PS,EINDEUTIG,EINGABENOTWENDIG,BEARBERLAUBT,SYSTEMWERT,MINIMUM,MAXIMUM,SPALTENTYP,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,TABDEFID) 
values ('31','VerdeckenDurchSpalteAKey','VerdeckenDurchSpalteAKey','STRING','0',null,'0','-1','0','0','255',null,'MW',to_date('03.07.18','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_TabSpEig_K:DSID'),
(select dsid from FX_TabDef_K where tabelle = 'FX_AnsichtSpalten_K'));

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_TabSpEig_K:DSID';

Insert into FX_TABSPEIG_K (REIHENFOLGE,SPALTE,SPALTENKOPF,DBDATENTYP,PS,EINDEUTIG,EINGABENOTWENDIG,BEARBERLAUBT,SYSTEMWERT,MINIMUM,MAXIMUM,SPALTENTYP,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,TABDEFID) 
values ('30','VerdeckenDurchTabAKey','VerdeckenDurchTabAKey','STRING','0',null,'0','-1','0','0','255',null,'MW',to_date('03.07.18','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_TabSpEig_K:DSID'),
(select dsid from FX_TabDef_K where tabelle = 'FX_AnsichtSpalten_K'));

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_TabSpEig_K:DSID';

Insert into FX_TABSPEIG_K (REIHENFOLGE,SPALTE,SPALTENKOPF,DBDATENTYP,PS,EINDEUTIG,EINGABENOTWENDIG,BEARBERLAUBT,SYSTEMWERT,MINIMUM,MAXIMUM,SPALTENTYP,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,TABDEFID) 
values ('21','AnsichtSuchen','AnsichtSuchen','STRING','0',null,'0','-1','0','0','255',null,'MW',to_date('03.07.18','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_TabSpEig_K:DSID'),
(select dsid from FX_TabDef_K where tabelle = 'FX_AnsichtSpalten_K'));

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_TabSpEig_K:DSID';

Insert into FX_TABSPEIG_K (REIHENFOLGE,SPALTE,SPALTENKOPF,DBDATENTYP,PS,EINDEUTIG,EINGABENOTWENDIG,BEARBERLAUBT,SYSTEMWERT,MINIMUM,MAXIMUM,SPALTENTYP,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,TABDEFID) 
values ('12','AnzahlNachkommastellen','AnzahlNachkommastellen','BYTE','0',null,'0','-1','0',null,null,null,'MW',to_date('03.07.18','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_TabSpEig_K:DSID'),
(select dsid from FX_TabDef_K where tabelle = 'FX_AnsichtSpalten_K'));

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_TabSpEig_K:DSID';

Insert into FX_TABSPEIG_K (REIHENFOLGE,SPALTE,SPALTENKOPF,DBDATENTYP,PS,EINDEUTIG,EINGABENOTWENDIG,BEARBERLAUBT,SYSTEMWERT,MINIMUM,MAXIMUM,SPALTENTYP,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,TABDEFID) 
values ('9','Filter','Filter','LONGINTEGER','0',null,'0','-1','0',null,null,null,'MW',to_date('03.07.18','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_TabSpEig_K:DSID'),
(select dsid from FX_TabDef_K where tabelle = 'FX_AnsichtSpalten_K'));

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_TabSpEig_K:DSID';

Insert into FX_TABSPEIG_K (REIHENFOLGE,SPALTE,SPALTENKOPF,DBDATENTYP,PS,EINDEUTIG,EINGABENOTWENDIG,BEARBERLAUBT,SYSTEMWERT,MINIMUM,MAXIMUM,SPALTENTYP,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,TABDEFID) 
values ('26','WhereSpalteAKey','WhereSpalteAKey','STRING','0',null,'0','-1','0','0','255',null,'MW',to_date('03.07.18','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_TabSpEig_K:DSID'),
(select dsid from FX_TabDef_K where tabelle = 'FX_AnsichtSpalten_K'));

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_TabSpEig_K:DSID';

Insert into FX_TABSPEIG_K (REIHENFOLGE,SPALTE,SPALTENKOPF,DBDATENTYP,PS,EINDEUTIG,EINGABENOTWENDIG,BEARBERLAUBT,SYSTEMWERT,MINIMUM,MAXIMUM,SPALTENTYP,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,TABDEFID) 
values ('25','WhereTabAKey','WhereTabAKey','STRING','0',null,'0','-1','0','0','255',null,'MW',to_date('03.07.18','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_TabSpEig_K:DSID'),
(select dsid from FX_TabDef_K where tabelle = 'FX_AnsichtSpalten_K'));

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_TabSpEig_K:DSID';

-- insert Datasets for FX_TabSpEig_K for FX_Menues_K
Insert into FX_TABSPEIG_K (REIHENFOLGE,SPALTE,SPALTENKOPF,DBDATENTYP,PS,EINDEUTIG,EINGABENOTWENDIG,BEARBERLAUBT,SYSTEMWERT,MINIMUM,MAXIMUM,SPALTENTYP,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,TABDEFID) 
values ('19','angelegtAm','angelegt_am','DATETIME','0',null,'-1','-1','-1',null,null,'AngelegtAm','MW',to_date('03.07.18','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_TabSpEig_K:DSID'),
(select dsid from FX_TabDef_K where tabelle = 'FX_Menues_K'));

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_TabSpEig_K:DSID';

Insert into FX_TABSPEIG_K (REIHENFOLGE,SPALTE,SPALTENKOPF,DBDATENTYP,PS,EINDEUTIG,EINGABENOTWENDIG,BEARBERLAUBT,SYSTEMWERT,MINIMUM,MAXIMUM,SPALTENTYP,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,TABDEFID) 
values ('18','angelegtVon','angelegt_von','STRING','0',null,'-1','-1','-1','0','255','AngelegtVon','MW',to_date('03.07.18','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_TabSpEig_K:DSID'),
(select dsid from FX_TabDef_K where tabelle = 'FX_Menues_K'));

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_TabSpEig_K:DSID';

Insert into FX_TABSPEIG_K (REIHENFOLGE,SPALTE,SPALTENKOPF,DBDATENTYP,PS,EINDEUTIG,EINGABENOTWENDIG,BEARBERLAUBT,SYSTEMWERT,MINIMUM,MAXIMUM,SPALTENTYP,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,TABDEFID) 
values ('3','AnzeigeName','AnzeigeName','STRING','0',null,'-1','-1','0','0','255',null,'MW',to_date('03.07.18','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_TabSpEig_K:DSID'),
(select dsid from FX_TabDef_K where tabelle = 'FX_Menues_K'));

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_TabSpEig_K:DSID';

Insert into FX_TABSPEIG_K (REIHENFOLGE,SPALTE,SPALTENKOPF,DBDATENTYP,PS,EINDEUTIG,EINGABENOTWENDIG,BEARBERLAUBT,SYSTEMWERT,MINIMUM,MAXIMUM,SPALTENTYP,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,TABDEFID) 
values ('20','DSID','DSID','LONGINTEGER','-1',null,'-1','-1','-1',null,null,'LaufendeNummer','MW',to_date('03.07.18','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_TabSpEig_K:DSID'),
(select dsid from FX_TabDef_K where tabelle = 'FX_Menues_K'));

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_TabSpEig_K:DSID';

Insert into FX_TABSPEIG_K (REIHENFOLGE,SPALTE,SPALTENKOPF,DBDATENTYP,PS,EINDEUTIG,EINGABENOTWENDIG,BEARBERLAUBT,SYSTEMWERT,MINIMUM,MAXIMUM,SPALTENTYP,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,TABDEFID) 
values ('5','Ebene','Ebene','LONGINTEGER','0',null,'-1','-1','0',null,null,null,'MW',to_date('03.07.18','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_TabSpEig_K:DSID'),
(select dsid from FX_TabDef_K where tabelle = 'FX_Menues_K'));

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_TabSpEig_K:DSID';

Insert into FX_TABSPEIG_K (REIHENFOLGE,SPALTE,SPALTENKOPF,DBDATENTYP,PS,EINDEUTIG,EINGABENOTWENDIG,BEARBERLAUBT,SYSTEMWERT,MINIMUM,MAXIMUM,SPALTENTYP,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,TABDEFID) 
values ('17','Gruppe','Gruppe','STRING','0',null,'0','-1','0','0','255',null,'MW',to_date('03.07.18','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_TabSpEig_K:DSID'),
(select dsid from FX_TabDef_K where tabelle = 'FX_Menues_K'));

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_TabSpEig_K:DSID';

Insert into FX_TABSPEIG_K (REIHENFOLGE,SPALTE,SPALTENKOPF,DBDATENTYP,PS,EINDEUTIG,EINGABENOTWENDIG,BEARBERLAUBT,SYSTEMWERT,MINIMUM,MAXIMUM,SPALTENTYP,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,TABDEFID) 
values ('10','Lizenz','Lizenz','STRING','0',null,'0','-1','0','0','255',null,'MW',to_date('03.07.18','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_TabSpEig_K:DSID'),
(select dsid from FX_TabDef_K where tabelle = 'FX_Menues_K'));

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_TabSpEig_K:DSID';

Insert into FX_TABSPEIG_K (REIHENFOLGE,SPALTE,SPALTENKOPF,DBDATENTYP,PS,EINDEUTIG,EINGABENOTWENDIG,BEARBERLAUBT,SYSTEMWERT,MINIMUM,MAXIMUM,SPALTENTYP,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,TABDEFID) 
values ('2','Menue','Menue','STRING','0',null,'-1','-1','0','0','255',null,'MW',to_date('03.07.18','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_TabSpEig_K:DSID'),
(select dsid from FX_TabDef_K where tabelle = 'FX_Menues_K'));

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_TabSpEig_K:DSID';

Insert into FX_TABSPEIG_K (REIHENFOLGE,SPALTE,SPALTENKOPF,DBDATENTYP,PS,EINDEUTIG,EINGABENOTWENDIG,BEARBERLAUBT,SYSTEMWERT,MINIMUM,MAXIMUM,SPALTENTYP,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,TABDEFID) 
values ('1','MenueID','MenueID','LONGINTEGER','0','10','-1','-1','0',null,null,null,'MW',to_date('03.07.18','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_TabSpEig_K:DSID'),
(select dsid from FX_TabDef_K where tabelle = 'FX_Menues_K'));

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_TabSpEig_K:DSID';

Insert into FX_TABSPEIG_K (REIHENFOLGE,SPALTE,SPALTENKOPF,DBDATENTYP,PS,EINDEUTIG,EINGABENOTWENDIG,BEARBERLAUBT,SYSTEMWERT,MINIMUM,MAXIMUM,SPALTENTYP,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,TABDEFID) 
values ('4','Typ','Typ','STRING','0',null,'-1','-1','0','0','255',null,'MW',to_date('03.07.18','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_TabSpEig_K:DSID'),
(select dsid from FX_TabDef_K where tabelle = 'FX_Menues_K'));

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_TabSpEig_K:DSID';

Insert into FX_TABSPEIG_K (REIHENFOLGE,SPALTE,SPALTENKOPF,DBDATENTYP,PS,EINDEUTIG,EINGABENOTWENDIG,BEARBERLAUBT,SYSTEMWERT,MINIMUM,MAXIMUM,SPALTENTYP,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,TABDEFID) 
values ('22','BereichsId','Bereichs_ID','LONGINTEGER','0',null,'0','-1','0',null,null,null,'MW',to_date('28.02.12','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_TabSpEig_K:DSID'),
(select dsid from FX_TabDef_K where tabelle = 'FX_Menues_K'));

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_TabSpEig_K:DSID';

Insert into FX_TABSPEIG_K (REIHENFOLGE,SPALTE,SPALTENKOPF,DBDATENTYP,PS,EINDEUTIG,EINGABENOTWENDIG,BEARBERLAUBT,SYSTEMWERT,MINIMUM,MAXIMUM,SPALTENTYP,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,TABDEFID) 
values ('6','UntermenueVon','UntermenueVon','STRING','0',null,'0','-1','0','0','255',null,'MW',to_date('03.07.18','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_TabSpEig_K:DSID'),
(select dsid from FX_TabDef_K where tabelle = 'FX_Menues_K'));

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_TabSpEig_K:DSID';

Insert into FX_TABSPEIG_K (REIHENFOLGE,SPALTE,SPALTENKOPF,DBDATENTYP,PS,EINDEUTIG,EINGABENOTWENDIG,BEARBERLAUBT,SYSTEMWERT,MINIMUM,MAXIMUM,SPALTENTYP,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,TABDEFID) 
values ('23','urlPath','UrlPath','STRING','0',null,'-1','-1','0',null,null,null,'MW',to_date('02.03.12','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_TabSpEig_K:DSID'),
(select dsid from FX_TabDef_K where tabelle = 'FX_Menues_K'));

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_TabSpEig_K:DSID';

Insert into FX_TABSPEIG_K (REIHENFOLGE,SPALTE,SPALTENKOPF,DBDATENTYP,PS,EINDEUTIG,EINGABENOTWENDIG,BEARBERLAUBT,SYSTEMWERT,MINIMUM,MAXIMUM,SPALTENTYP,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,TABDEFID) 
values ('24','aktiv','aktiv','BOOLEAN','0',null,'-1','-1','0',null,null,null,'MW',to_date('04.03.12','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_TabSpEig_K:DSID'),
(select dsid from FX_TabDef_K where tabelle = 'FX_Menues_K'));

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_TabSpEig_K:DSID';

Insert into FX_TABSPEIG_K (REIHENFOLGE,SPALTE,SPALTENKOPF,DBDATENTYP,PS,EINDEUTIG,EINGABENOTWENDIG,BEARBERLAUBT,SYSTEMWERT,MINIMUM,MAXIMUM,SPALTENTYP,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,TABDEFID) 
values ('25','AnsichtDefId','AnsichtDefId','LONGINTEGER','0','-1','0','-1','0',null,null,null,'MW',to_date('14.03.12','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_TabSpEig_K:DSID'),
(select dsid from FX_TabDef_K where tabelle = 'FX_Menues_K'));

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_TabSpEig_K:DSID';

Insert into FX_TABSPEIG_K (REIHENFOLGE,SPALTE,SPALTENKOPF,DBDATENTYP,PS,EINDEUTIG,EINGABENOTWENDIG,BEARBERLAUBT,SYSTEMWERT,MINIMUM,MAXIMUM,SPALTENTYP,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,TABDEFID) 
values ('26','HauptMenueId','HauptMenueId','LONGINTEGER','0','0','0','-1','0',null,null,null,'MW',to_date('16.03.12','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_TabSpEig_K:DSID'),
(select dsid from FX_TabDef_K where tabelle = 'FX_Menues_K'));

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_TabSpEig_K:DSID';

Insert into FX_TABSPEIG_K (REIHENFOLGE,SPALTE,SPALTENKOPF,DBDATENTYP,PS,EINDEUTIG,EINGABENOTWENDIG,BEARBERLAUBT,SYSTEMWERT,MINIMUM,MAXIMUM,SPALTENTYP,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,TABDEFID) 
values ('21','System','System','BOOLEAN','0',null,'-1','-1','0',null,null,null,'MW',to_date('03.07.18','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_TabSpEig_K:DSID'),
(select dsid from FX_TabDef_K where tabelle = 'FX_Menues_K'));

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_TabSpEig_K:DSID';

-- insert Datasets for FX_TabSpEig_K for BenutzerBereicheDef
Insert into FX_TABSPEIG_K (REIHENFOLGE,SPALTE,SPALTENKOPF,DBDATENTYP,PS,EINDEUTIG,EINGABENOTWENDIG,BEARBERLAUBT,SYSTEMWERT,MINIMUM,MAXIMUM,SPALTENTYP,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,TABDEFID) 
values ('12','abrechnen','abrechnen','BOOLEAN','0',null,'-1','-1','0',null,null,null,'MW',to_date('14.05.02','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_TabSpEig_K:DSID'),
(select dsid from FX_TabDef_K where tabelle = 'BenutzerBereicheDef'));

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_TabSpEig_K:DSID';

Insert into FX_TABSPEIG_K (REIHENFOLGE,SPALTE,SPALTENKOPF,DBDATENTYP,PS,EINDEUTIG,EINGABENOTWENDIG,BEARBERLAUBT,SYSTEMWERT,MINIMUM,MAXIMUM,SPALTENTYP,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,TABDEFID) 
values ('253','AngelegtAm','angelegt_am','DATETIME','0',null,'-1','-1','-1',null,null,'AngelegtAm','KG',to_date('10.09.07','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_TabSpEig_K:DSID'),
(select dsid from FX_TabDef_K where tabelle = 'BenutzerBereicheDef'));

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_TabSpEig_K:DSID';

Insert into FX_TABSPEIG_K (REIHENFOLGE,SPALTE,SPALTENKOPF,DBDATENTYP,PS,EINDEUTIG,EINGABENOTWENDIG,BEARBERLAUBT,SYSTEMWERT,MINIMUM,MAXIMUM,SPALTENTYP,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,TABDEFID) 
values ('254','AngelegtVon','angelegt_von','LONGINTEGER','0',null,'-1','-1','-1',null,null,'AngelegtVon','KG',to_date('10.09.07','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_TabSpEig_K:DSID'),
(select dsid from FX_TabDef_K where tabelle = 'BenutzerBereicheDef'));

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_TabSpEig_K:DSID';

Insert into FX_TABSPEIG_K (REIHENFOLGE,SPALTE,SPALTENKOPF,DBDATENTYP,PS,EINDEUTIG,EINGABENOTWENDIG,BEARBERLAUBT,SYSTEMWERT,MINIMUM,MAXIMUM,SPALTENTYP,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,TABDEFID) 
values ('11','berechnen','berechnen','BOOLEAN','0',null,'-1','-1','0',null,null,null,'MW',to_date('14.05.02','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_TabSpEig_K:DSID'),
(select dsid from FX_TabDef_K where tabelle = 'BenutzerBereicheDef'));

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_TabSpEig_K:DSID';

Insert into FX_TABSPEIG_K (REIHENFOLGE,SPALTE,SPALTENKOPF,DBDATENTYP,PS,EINDEUTIG,EINGABENOTWENDIG,BEARBERLAUBT,SYSTEMWERT,MINIMUM,MAXIMUM,SPALTENTYP,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,TABDEFID) 
values ('1','Name','Bereich','STRING','0','1','-1','-1','0','2','50',null,'MW',to_date('06.05.02','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_TabSpEig_K:DSID'),
(select dsid from FX_TabDef_K where tabelle = 'BenutzerBereicheDef'));

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_TabSpEig_K:DSID';

Insert into FX_TABSPEIG_K (REIHENFOLGE,SPALTE,SPALTENKOPF,DBDATENTYP,PS,EINDEUTIG,EINGABENOTWENDIG,BEARBERLAUBT,SYSTEMWERT,MINIMUM,MAXIMUM,SPALTENTYP,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,TABDEFID) 
values ('10','stornieren','stornieren','BOOLEAN','0',null,'-1','-1','0',null,null,null,'MW',to_date('14.05.02','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_TabSpEig_K:DSID'),
(select dsid from FX_TabDef_K where tabelle = 'BenutzerBereicheDef'));

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_TabSpEig_K:DSID';

Insert into FX_TABSPEIG_K (REIHENFOLGE,SPALTE,SPALTENKOPF,DBDATENTYP,PS,EINDEUTIG,EINGABENOTWENDIG,BEARBERLAUBT,SYSTEMWERT,MINIMUM,MAXIMUM,SPALTENTYP,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,TABDEFID) 
values ('256','zuletztBearbeitetVon','bearb_von','LONGINTEGER','0',null,'0','-1','-1',null,null,'ZuletztBearbeitetVon','KG',to_date('10.09.07','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_TabSpEig_K:DSID'),
(select dsid from FX_TabDef_K where tabelle = 'BenutzerBereicheDef'));

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_TabSpEig_K:DSID';

Insert into FX_TABSPEIG_K (REIHENFOLGE,SPALTE,SPALTENKOPF,DBDATENTYP,PS,EINDEUTIG,EINGABENOTWENDIG,BEARBERLAUBT,SYSTEMWERT,MINIMUM,MAXIMUM,SPALTENTYP,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,TABDEFID) 
values ('257','zuletztGeaendertAm','bearb_am','DATETIME','0',null,'0','-1','-1',null,null,'ZuletztGeaendertAm','KG',to_date('10.09.07','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_TabSpEig_K:DSID'),
(select dsid from FX_TabDef_K where tabelle = 'BenutzerBereicheDef'));

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_TabSpEig_K:DSID';

Insert into FX_TABSPEIG_K (REIHENFOLGE,SPALTE,SPALTENKOPF,DBDATENTYP,PS,EINDEUTIG,EINGABENOTWENDIG,BEARBERLAUBT,SYSTEMWERT,MINIMUM,MAXIMUM,SPALTENTYP,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,TABDEFID) 
values ('16','AlleDs','Alle_DS','BOOLEAN','0',null,'-1','-1','0',null,null,null,'MW',to_date('14.09.02','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_TabSpEig_K:DSID'),
(select dsid from FX_TabDef_K where tabelle = 'BenutzerBereicheDef'));

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_TabSpEig_K:DSID';

Insert into FX_TABSPEIG_K (REIHENFOLGE,SPALTE,SPALTENKOPF,DBDATENTYP,PS,EINDEUTIG,EINGABENOTWENDIG,BEARBERLAUBT,SYSTEMWERT,MINIMUM,MAXIMUM,SPALTENTYP,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,TABDEFID) 
values ('8','hinzufuegen','hinzufügen','BOOLEAN','0',null,'-1','-1','0',null,null,null,'MW',to_date('14.05.02','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_TabSpEig_K:DSID'),
(select dsid from FX_TabDef_K where tabelle = 'BenutzerBereicheDef'));

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_TabSpEig_K:DSID';

Insert into FX_TABSPEIG_K (REIHENFOLGE,SPALTE,SPALTENKOPF,DBDATENTYP,PS,EINDEUTIG,EINGABENOTWENDIG,BEARBERLAUBT,SYSTEMWERT,MINIMUM,MAXIMUM,SPALTENTYP,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,TABDEFID) 
values ('2','Beschreibung','Beschreibung','STRING','0',null,'-1','-1','0','1','255',null,'PK',to_date('01.01.99','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_TabSpEig_K:DSID'),
(select dsid from FX_TabDef_K where tabelle = 'BenutzerBereicheDef'));

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_TabSpEig_K:DSID';

Insert into FX_TABSPEIG_K (REIHENFOLGE,SPALTE,SPALTENKOPF,DBDATENTYP,PS,EINDEUTIG,EINGABENOTWENDIG,BEARBERLAUBT,SYSTEMWERT,MINIMUM,MAXIMUM,SPALTENTYP,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,TABDEFID) 
values ('4','lesen','lesen','BOOLEAN','0',null,'-1','-1','0',null,null,null,'MW',to_date('14.05.02','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_TabSpEig_K:DSID'),
(select dsid from FX_TabDef_K where tabelle = 'BenutzerBereicheDef'));

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_TabSpEig_K:DSID';

Insert into FX_TABSPEIG_K (REIHENFOLGE,SPALTE,SPALTENKOPF,DBDATENTYP,PS,EINDEUTIG,EINGABENOTWENDIG,BEARBERLAUBT,SYSTEMWERT,MINIMUM,MAXIMUM,SPALTENTYP,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,TABDEFID) 
values ('0','BereichsId','Bereichs_ID','LONGINTEGER','-1',null,'-1','-1','-1',null,null,'LaufendeNummer','MW',to_date('06.05.02','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_TabSpEig_K:DSID'),
(select dsid from FX_TabDef_K where tabelle = 'BenutzerBereicheDef'));

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_TabSpEig_K:DSID';

Insert into FX_TABSPEIG_K (REIHENFOLGE,SPALTE,SPALTENKOPF,DBDATENTYP,PS,EINDEUTIG,EINGABENOTWENDIG,BEARBERLAUBT,SYSTEMWERT,MINIMUM,MAXIMUM,SPALTENTYP,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,TABDEFID) 
values ('14','Unterschrift1','Unterschrift_1','BOOLEAN','0',null,'-1','-1','0',null,null,null,'PK',to_date('25.08.02','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_TabSpEig_K:DSID'),
(select dsid from FX_TabDef_K where tabelle = 'BenutzerBereicheDef'));

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_TabSpEig_K:DSID';

Insert into FX_TABSPEIG_K (REIHENFOLGE,SPALTE,SPALTENKOPF,DBDATENTYP,PS,EINDEUTIG,EINGABENOTWENDIG,BEARBERLAUBT,SYSTEMWERT,MINIMUM,MAXIMUM,SPALTENTYP,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,TABDEFID) 
values ('7','bearbeiten','verändern','BOOLEAN','0',null,'-1','-1','0',null,null,null,'MW',to_date('14.05.02','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_TabSpEig_K:DSID'),
(select dsid from FX_TabDef_K where tabelle = 'BenutzerBereicheDef'));

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_TabSpEig_K:DSID';

Insert into FX_TABSPEIG_K (REIHENFOLGE,SPALTE,SPALTENKOPF,DBDATENTYP,PS,EINDEUTIG,EINGABENOTWENDIG,BEARBERLAUBT,SYSTEMWERT,MINIMUM,MAXIMUM,SPALTENTYP,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,TABDEFID) 
values ('9','loeschen','löschen','BOOLEAN','0',null,'-1','-1','0',null,null,null,'MW',to_date('14.05.02','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_TabSpEig_K:DSID'),
(select dsid from FX_TabDef_K where tabelle = 'BenutzerBereicheDef'));

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_TabSpEig_K:DSID';

-- *************************************************************
-- insert  FX_AnsichtSpalten datasets for BENUTZERBEREICHEDEF
-- *************************************************************

-- insert FX_AnsichtSpalten datasets for 
Insert into FX_ANSICHTSPALTEN_K (INDEXGRID,TABAKEY,SPALTEAKEY,INGRIDANZEIGEN,INGRIDLADEN,FILTER,ANZAHLNACHKOMMASTELLEN,BEARBHINZUFZUGELASSEN,BEARBZUGELASSEN,EINGABENOTWENDIG,ANSICHTSUCHEN,SUCHWERTAUSTABAKEY,SUCHWERTAUSSPALTEAKEY,WHERETABAKEY,WHERESPALTEAKEY,VERDECKENDURCHTABAKEY,VERDECKENDURCHSPALTEAKEY,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,ANSICHTDEFID,ANSICHTTABID,TABSPEIGID) values ('52','BenutzerBereicheDef','AngelegtAm','-1','-1','-1',null,null,null,null,null,null,null,null,null,null,null,'MW',to_date('04.07.18','DD.MM.RR'),'-1',(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_AnsichtSpalten_K:DSID'),
(select dsid from FX_AnsichtDef_K where ansicht = 'BenutzerBereicheDef'),
(select dsid from FX_AnsichtTab_K where ansicht = 'BenutzerBereicheDef'),
(select FX_TabSpEig_K.dsid from FX_TabSpEig_K join FX_TabDef_K on FX_TabSpEig_K.TABDEFID = FX_TabDef_K.DSID 
where FX_TabDef_K.tabelle = 'BenutzerBereicheDef' and FX_TabSpEig_K.spalte = 'AngelegtAm'));

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_AnsichtSpalten_K:DSID';

Insert into FX_ANSICHTSPALTEN_K (INDEXGRID,TABAKEY,SPALTEAKEY,INGRIDANZEIGEN,INGRIDLADEN,FILTER,ANZAHLNACHKOMMASTELLEN,BEARBHINZUFZUGELASSEN,BEARBZUGELASSEN,EINGABENOTWENDIG,ANSICHTSUCHEN,SUCHWERTAUSTABAKEY,SUCHWERTAUSSPALTEAKEY,WHERETABAKEY,WHERESPALTEAKEY,VERDECKENDURCHTABAKEY,VERDECKENDURCHSPALTEAKEY,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,ANSICHTDEFID,ANSICHTTABID,TABSPEIGID) values ('53','BenutzerBereicheDef','AngelegtVon',null,'-1','-1',null,null,null,null,null,null,null,null,null,null,null,'MW',to_date('04.07.18','DD.MM.RR'),'-1',(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_AnsichtSpalten_K:DSID'),
(select dsid from FX_AnsichtDef_K where ansicht = 'BenutzerBereicheDef'),
(select dsid from FX_AnsichtTab_K where ansicht = 'BenutzerBereicheDef'),
(select FX_TabSpEig_K.dsid from FX_TabSpEig_K join FX_TabDef_K on FX_TabSpEig_K.TABDEFID = FX_TabDef_K.DSID where FX_TabDef_K.tabelle = 'BenutzerBereicheDef' and FX_TabSpEig_K.spalte = 'AngelegtVon'));

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_AnsichtSpalten_K:DSID';

Insert into FX_ANSICHTSPALTEN_K (INDEXGRID,TABAKEY,SPALTEAKEY,INGRIDANZEIGEN,INGRIDLADEN,FILTER,ANZAHLNACHKOMMASTELLEN,BEARBHINZUFZUGELASSEN,BEARBZUGELASSEN,EINGABENOTWENDIG,ANSICHTSUCHEN,SUCHWERTAUSTABAKEY,SUCHWERTAUSSPALTEAKEY,WHERETABAKEY,WHERESPALTEAKEY,VERDECKENDURCHTABAKEY,VERDECKENDURCHSPALTEAKEY,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,ANSICHTDEFID,ANSICHTTABID,TABSPEIGID) values ('2','BenutzerBereicheDef','BereichsId',null,'-1','-1',null,null,null,null,null,null,null,null,null,null,null,'MW',to_date('04.07.18','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_AnsichtSpalten_K:DSID'),
(select dsid from FX_AnsichtDef_K where ansicht = 'BenutzerBereicheDef'),
(select dsid from FX_AnsichtTab_K where ansicht = 'BenutzerBereicheDef'),
(select FX_TabSpEig_K.dsid from FX_TabSpEig_K join FX_TabDef_K on FX_TabSpEig_K.TABDEFID = FX_TabDef_K.DSID where FX_TabDef_K.tabelle = 'BenutzerBereicheDef' and FX_TabSpEig_K.spalte = 'BereichsId'));

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_AnsichtSpalten_K:DSID';

Insert into FX_ANSICHTSPALTEN_K (INDEXGRID,TABAKEY,SPALTEAKEY,INGRIDANZEIGEN,INGRIDLADEN,FILTER,ANZAHLNACHKOMMASTELLEN,BEARBHINZUFZUGELASSEN,BEARBZUGELASSEN,EINGABENOTWENDIG,ANSICHTSUCHEN,SUCHWERTAUSTABAKEY,SUCHWERTAUSSPALTEAKEY,WHERETABAKEY,WHERESPALTEAKEY,VERDECKENDURCHTABAKEY,VERDECKENDURCHSPALTEAKEY,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,ANSICHTDEFID,ANSICHTTABID,TABSPEIGID) values ('1','BenutzerBereicheDef','Beschreibung','-1','-1','-1',null,'-1','-1',null,null,null,null,null,null,null,null,'MW',to_date('04.07.18','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_AnsichtSpalten_K:DSID'),
(select dsid from FX_AnsichtDef_K where ansicht = 'BenutzerBereicheDef'),
(select dsid from FX_AnsichtTab_K where ansicht = 'BenutzerBereicheDef'),
(select FX_TabSpEig_K.dsid from FX_TabSpEig_K join FX_TabDef_K on FX_TabSpEig_K.TABDEFID = FX_TabDef_K.DSID where FX_TabDef_K.tabelle = 'BenutzerBereicheDef' and FX_TabSpEig_K.spalte = 'Beschreibung'));

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_AnsichtSpalten_K:DSID';

Insert into FX_ANSICHTSPALTEN_K (INDEXGRID,TABAKEY,SPALTEAKEY,INGRIDANZEIGEN,INGRIDLADEN,FILTER,ANZAHLNACHKOMMASTELLEN,BEARBHINZUFZUGELASSEN,BEARBZUGELASSEN,EINGABENOTWENDIG,ANSICHTSUCHEN,SUCHWERTAUSTABAKEY,SUCHWERTAUSSPALTEAKEY,WHERETABAKEY,WHERESPALTEAKEY,VERDECKENDURCHTABAKEY,VERDECKENDURCHSPALTEAKEY,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,ANSICHTDEFID,ANSICHTTABID,TABSPEIGID) values ('0','BenutzerBereicheDef','Name','-1','-1','-1',null,'-1','-1',null,null,null,null,null,null,null,null,'MW',to_date('04.07.18','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_AnsichtSpalten_K:DSID'),
(select dsid from FX_AnsichtDef_K where ansicht = 'BenutzerBereicheDef'),
(select dsid from FX_AnsichtTab_K where ansicht = 'BenutzerBereicheDef'),
(select FX_TabSpEig_K.dsid from FX_TabSpEig_K join FX_TabDef_K on FX_TabSpEig_K.TABDEFID = FX_TabDef_K.DSID where FX_TabDef_K.tabelle = 'BenutzerBereicheDef' and FX_TabSpEig_K.spalte = 'Name'));

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_AnsichtSpalten_K:DSID';

Insert into FX_ANSICHTSPALTEN_K (INDEXGRID,TABAKEY,SPALTEAKEY,INGRIDANZEIGEN,INGRIDLADEN,FILTER,ANZAHLNACHKOMMASTELLEN,BEARBHINZUFZUGELASSEN,BEARBZUGELASSEN,EINGABENOTWENDIG,ANSICHTSUCHEN,SUCHWERTAUSTABAKEY,SUCHWERTAUSSPALTEAKEY,WHERETABAKEY,WHERESPALTEAKEY,VERDECKENDURCHTABAKEY,VERDECKENDURCHSPALTEAKEY,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,ANSICHTDEFID,ANSICHTTABID,TABSPEIGID) values ('12','BenutzerBereicheDef','Vergebbar',null,null,'-1',null,'-1','-1',null,null,null,null,null,null,null,null,'MW',to_date('04.07.18','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_AnsichtSpalten_K:DSID'),
(select dsid from FX_AnsichtDef_K where ansicht = 'BenutzerBereicheDef'),
(select dsid from FX_AnsichtTab_K where ansicht = 'BenutzerBereicheDef'),
(select FX_TabSpEig_K.dsid from FX_TabSpEig_K join FX_TabDef_K on FX_TabSpEig_K.TABDEFID = FX_TabDef_K.DSID where FX_TabDef_K.tabelle = 'BenutzerBereicheDef' and FX_TabSpEig_K.spalte = 'Vergebbar'));

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_AnsichtSpalten_K:DSID';

Insert into FX_ANSICHTSPALTEN_K (INDEXGRID,TABAKEY,SPALTEAKEY,INGRIDANZEIGEN,INGRIDLADEN,FILTER,ANZAHLNACHKOMMASTELLEN,BEARBHINZUFZUGELASSEN,BEARBZUGELASSEN,EINGABENOTWENDIG,ANSICHTSUCHEN,SUCHWERTAUSTABAKEY,SUCHWERTAUSSPALTEAKEY,WHERETABAKEY,WHERESPALTEAKEY,VERDECKENDURCHTABAKEY,VERDECKENDURCHSPALTEAKEY,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,ANSICHTDEFID,ANSICHTTABID,TABSPEIGID) values ('6','BenutzerBereicheDef','bearbeiten',null,null,'-1',null,'-1','-1',null,null,null,null,null,null,null,null,'MW',to_date('04.07.18','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_AnsichtSpalten_K:DSID'),
(select dsid from FX_AnsichtDef_K where ansicht = 'BenutzerBereicheDef'),
(select dsid from FX_AnsichtTab_K where ansicht = 'BenutzerBereicheDef'),
(select FX_TabSpEig_K.dsid from FX_TabSpEig_K join FX_TabDef_K on FX_TabSpEig_K.TABDEFID = FX_TabDef_K.DSID where FX_TabDef_K.tabelle = 'BenutzerBereicheDef' and FX_TabSpEig_K.spalte = 'bearbeiten'));

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_AnsichtSpalten_K:DSID';

Insert into FX_ANSICHTSPALTEN_K (INDEXGRID,TABAKEY,SPALTEAKEY,INGRIDANZEIGEN,INGRIDLADEN,FILTER,ANZAHLNACHKOMMASTELLEN,BEARBHINZUFZUGELASSEN,BEARBZUGELASSEN,EINGABENOTWENDIG,ANSICHTSUCHEN,SUCHWERTAUSTABAKEY,SUCHWERTAUSSPALTEAKEY,WHERETABAKEY,WHERESPALTEAKEY,VERDECKENDURCHTABAKEY,VERDECKENDURCHSPALTEAKEY,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,ANSICHTDEFID,ANSICHTTABID,TABSPEIGID) values ('7','BenutzerBereicheDef','hinzufuegen',null,null,'-1',null,'-1','-1',null,null,null,null,null,null,null,null,'MW',to_date('04.07.18','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_AnsichtSpalten_K:DSID'),
(select dsid from FX_AnsichtDef_K where ansicht = 'BenutzerBereicheDef'),
(select dsid from FX_AnsichtTab_K where ansicht = 'BenutzerBereicheDef'),
(select FX_TabSpEig_K.dsid from FX_TabSpEig_K join FX_TabDef_K on FX_TabSpEig_K.TABDEFID = FX_TabDef_K.DSID where FX_TabDef_K.tabelle = 'BenutzerBereicheDef' and FX_TabSpEig_K.spalte = 'hinzufuegen'));

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_AnsichtSpalten_K:DSID';

Insert into FX_ANSICHTSPALTEN_K (INDEXGRID,TABAKEY,SPALTEAKEY,INGRIDANZEIGEN,INGRIDLADEN,FILTER,ANZAHLNACHKOMMASTELLEN,BEARBHINZUFZUGELASSEN,BEARBZUGELASSEN,EINGABENOTWENDIG,ANSICHTSUCHEN,SUCHWERTAUSTABAKEY,SUCHWERTAUSSPALTEAKEY,WHERETABAKEY,WHERESPALTEAKEY,VERDECKENDURCHTABAKEY,VERDECKENDURCHSPALTEAKEY,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,ANSICHTDEFID,ANSICHTTABID,TABSPEIGID) values ('3','BenutzerBereicheDef','lesen',null,null,'-1',null,'-1','-1',null,null,null,null,null,null,null,null,'MW',to_date('04.07.18','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_AnsichtSpalten_K:DSID'),
(select dsid from FX_AnsichtDef_K where ansicht = 'BenutzerBereicheDef'),
(select dsid from FX_AnsichtTab_K where ansicht = 'BenutzerBereicheDef'),
(select FX_TabSpEig_K.dsid from FX_TabSpEig_K join FX_TabDef_K on FX_TabSpEig_K.TABDEFID = FX_TabDef_K.DSID where FX_TabDef_K.tabelle = 'BenutzerBereicheDef' and FX_TabSpEig_K.spalte = 'lesen'));

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_AnsichtSpalten_K:DSID';

Insert into FX_ANSICHTSPALTEN_K (INDEXGRID,TABAKEY,SPALTEAKEY,INGRIDANZEIGEN,INGRIDLADEN,FILTER,ANZAHLNACHKOMMASTELLEN,BEARBHINZUFZUGELASSEN,BEARBZUGELASSEN,EINGABENOTWENDIG,ANSICHTSUCHEN,SUCHWERTAUSTABAKEY,SUCHWERTAUSSPALTEAKEY,WHERETABAKEY,WHERESPALTEAKEY,VERDECKENDURCHTABAKEY,VERDECKENDURCHSPALTEAKEY,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,ANSICHTDEFID,ANSICHTTABID,TABSPEIGID) values ('8','BenutzerBereicheDef','loeschen',null,null,'-1',null,'-1','-1',null,null,null,null,null,null,null,null,'MW',to_date('04.07.18','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_AnsichtSpalten_K:DSID'),
(select dsid from FX_AnsichtDef_K where ansicht = 'BenutzerBereicheDef'),
(select dsid from FX_AnsichtTab_K where ansicht = 'BenutzerBereicheDef'),
(select FX_TabSpEig_K.dsid from FX_TabSpEig_K join FX_TabDef_K on FX_TabSpEig_K.TABDEFID = FX_TabDef_K.DSID where FX_TabDef_K.tabelle = 'BenutzerBereicheDef' and FX_TabSpEig_K.spalte = 'loeschen'));

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_AnsichtSpalten_K:DSID';

Insert into FX_ANSICHTSPALTEN_K (INDEXGRID,TABAKEY,SPALTEAKEY,INGRIDANZEIGEN,INGRIDLADEN,FILTER,ANZAHLNACHKOMMASTELLEN,BEARBHINZUFZUGELASSEN,BEARBZUGELASSEN,EINGABENOTWENDIG,ANSICHTSUCHEN,SUCHWERTAUSTABAKEY,SUCHWERTAUSSPALTEAKEY,WHERETABAKEY,WHERESPALTEAKEY,VERDECKENDURCHTABAKEY,VERDECKENDURCHSPALTEAKEY,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,ANSICHTDEFID,ANSICHTTABID,TABSPEIGID) values ('50','BenutzerBereicheDef','zuletztBearbeitetVon',null,null,'-1',null,null,null,null,null,null,null,null,null,null,null,'MW',to_date('04.07.18','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_AnsichtSpalten_K:DSID'),
(select dsid from FX_AnsichtDef_K where ansicht = 'BenutzerBereicheDef'),
(select dsid from FX_AnsichtTab_K where ansicht = 'BenutzerBereicheDef'),
(select FX_TabSpEig_K.dsid from FX_TabSpEig_K join FX_TabDef_K on FX_TabSpEig_K.TABDEFID = FX_TabDef_K.DSID where FX_TabDef_K.tabelle = 'BenutzerBereicheDef' and FX_TabSpEig_K.spalte = 'zuletztBearbeitetVon'));

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_AnsichtSpalten_K:DSID';

Insert into FX_ANSICHTSPALTEN_K (INDEXGRID,TABAKEY,SPALTEAKEY,INGRIDANZEIGEN,INGRIDLADEN,FILTER,ANZAHLNACHKOMMASTELLEN,BEARBHINZUFZUGELASSEN,BEARBZUGELASSEN,EINGABENOTWENDIG,ANSICHTSUCHEN,SUCHWERTAUSTABAKEY,SUCHWERTAUSSPALTEAKEY,WHERETABAKEY,WHERESPALTEAKEY,VERDECKENDURCHTABAKEY,VERDECKENDURCHSPALTEAKEY,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,ANSICHTDEFID,ANSICHTTABID,TABSPEIGID) values ('51','BenutzerBereicheDef','zuletztGeaendertAm',null,null,'-1',null,null,null,null,null,null,null,null,null,null,null,'MW',to_date('04.07.18','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_AnsichtSpalten_K:DSID'),
(select dsid from FX_AnsichtDef_K where ansicht = 'BenutzerBereicheDef'),
(select dsid from FX_AnsichtTab_K where ansicht = 'BenutzerBereicheDef'),
(select FX_TabSpEig_K.dsid from FX_TabSpEig_K join FX_TabDef_K on FX_TabSpEig_K.TABDEFID = FX_TabDef_K.DSID where FX_TabDef_K.tabelle = 'BenutzerBereicheDef' and FX_TabSpEig_K.spalte = 'zuletztGeaendertAm'));

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_AnsichtSpalten_K:DSID';

-- insert FX_AnsichtSpalten datasets for FX_TabDef_K
Insert into FX_ANSICHTSPALTEN_K (INDEXGRID,TABAKEY,SPALTEAKEY,INGRIDANZEIGEN,INGRIDLADEN,FILTER,ANZAHLNACHKOMMASTELLEN,BEARBHINZUFZUGELASSEN,BEARBZUGELASSEN,EINGABENOTWENDIG,ANSICHTSUCHEN,SUCHWERTAUSTABAKEY,SUCHWERTAUSSPALTEAKEY,WHERETABAKEY,WHERESPALTEAKEY,VERDECKENDURCHTABAKEY,VERDECKENDURCHSPALTEAKEY,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,ANSICHTDEFID,ANSICHTTABID,TABSPEIGID) values ('2','FX_TabDef_K','Alias','-1','-1','-1',null,null,null,null,null,null,null,null,null,null,null,'MW',to_date('04.07.18','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_AnsichtSpalten_K:DSID'),
(select dsid from FX_AnsichtDef_K where ansicht = 'FX_TabDef_K'),
(select dsid from FX_AnsichtTab_K where ansicht = 'FX_TabDef_K' and jointyp = 'x'),
(select FX_TabSpEig_K.dsid from FX_TabSpEig_K join FX_TabDef_K on FX_TabSpEig_K.TABDEFID = FX_TabDef_K.DSID where FX_TabDef_K.tabelle = 'FX_TabDef_K' and FX_TabSpEig_K.spalte = 'Alias'));

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_AnsichtSpalten_K:DSID';

Insert into FX_ANSICHTSPALTEN_K (INDEXGRID,TABAKEY,SPALTEAKEY,INGRIDANZEIGEN,INGRIDLADEN,FILTER,ANZAHLNACHKOMMASTELLEN,BEARBHINZUFZUGELASSEN,BEARBZUGELASSEN,EINGABENOTWENDIG,ANSICHTSUCHEN,SUCHWERTAUSTABAKEY,SUCHWERTAUSSPALTEAKEY,WHERETABAKEY,WHERESPALTEAKEY,VERDECKENDURCHTABAKEY,VERDECKENDURCHSPALTEAKEY,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,ANSICHTDEFID,ANSICHTTABID,TABSPEIGID) values ('25','FX_TabDef_K','BereichsId','-1','-1','-1',null,'-1',null,null,'BenutzerBereicheDef','BenutzerBereicheDef','BereichsId',null,null,'BenutzerBereicheDef','Name','MW#migration#',to_date('04.07.18','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_AnsichtSpalten_K:DSID'),
(select dsid from FX_AnsichtDef_K where ansicht = 'FX_TabDef_K'),
(select dsid from FX_AnsichtTab_K where ansicht = 'FX_TabDef_K' and jointyp = 'x'),
(select FX_TabSpEig_K.dsid from FX_TabSpEig_K join FX_TabDef_K on FX_TabSpEig_K.TABDEFID = FX_TabDef_K.DSID where FX_TabDef_K.tabelle = 'FX_TabDef_K' and FX_TabSpEig_K.spalte = 'BereichsId'));

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_AnsichtSpalten_K:DSID';

Insert into FX_ANSICHTSPALTEN_K (INDEXGRID,TABAKEY,SPALTEAKEY,INGRIDANZEIGEN,INGRIDLADEN,FILTER,ANZAHLNACHKOMMASTELLEN,BEARBHINZUFZUGELASSEN,BEARBZUGELASSEN,EINGABENOTWENDIG,ANSICHTSUCHEN,SUCHWERTAUSTABAKEY,SUCHWERTAUSSPALTEAKEY,WHERETABAKEY,WHERESPALTEAKEY,VERDECKENDURCHTABAKEY,VERDECKENDURCHSPALTEAKEY,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,ANSICHTDEFID,ANSICHTTABID,TABSPEIGID) values ('6','FX_TabDef_K','Bezeichnung','-1','-1','-1',null,null,null,null,null,null,null,null,null,null,null,'MW',to_date('04.07.18','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_AnsichtSpalten_K:DSID'),
(select dsid from FX_AnsichtDef_K where ansicht = 'FX_TabDef_K'),
(select dsid from FX_AnsichtTab_K where ansicht = 'FX_TabDef_K' and jointyp = 'x'),
(select FX_TabSpEig_K.dsid from FX_TabSpEig_K join FX_TabDef_K on FX_TabSpEig_K.TABDEFID = FX_TabDef_K.DSID where FX_TabDef_K.tabelle = 'FX_TabDef_K' and FX_TabSpEig_K.spalte = 'Bezeichnung'));

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_AnsichtSpalten_K:DSID';

Insert into FX_ANSICHTSPALTEN_K (INDEXGRID,TABAKEY,SPALTEAKEY,INGRIDANZEIGEN,INGRIDLADEN,FILTER,ANZAHLNACHKOMMASTELLEN,BEARBHINZUFZUGELASSEN,BEARBZUGELASSEN,EINGABENOTWENDIG,ANSICHTSUCHEN,SUCHWERTAUSTABAKEY,SUCHWERTAUSSPALTEAKEY,WHERETABAKEY,WHERESPALTEAKEY,VERDECKENDURCHTABAKEY,VERDECKENDURCHSPALTEAKEY,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,ANSICHTDEFID,ANSICHTTABID,TABSPEIGID) values ('20','FX_TabDef_K','DSID','-1','-1','-1',null,null,null,null,null,null,null,null,null,null,null,'MW',to_date('04.07.18','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_AnsichtSpalten_K:DSID'),
(select dsid from FX_AnsichtDef_K where ansicht = 'FX_TabDef_K'),
(select dsid from FX_AnsichtTab_K where ansicht = 'FX_TabDef_K' and jointyp = 'x'),
(select FX_TabSpEig_K.dsid from FX_TabSpEig_K join FX_TabDef_K on FX_TabSpEig_K.TABDEFID = FX_TabDef_K.DSID where FX_TabDef_K.tabelle = 'FX_TabDef_K' and FX_TabSpEig_K.spalte = 'DSID'));

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_AnsichtSpalten_K:DSID';

Insert into FX_ANSICHTSPALTEN_K (INDEXGRID,TABAKEY,SPALTEAKEY,INGRIDANZEIGEN,INGRIDLADEN,FILTER,ANZAHLNACHKOMMASTELLEN,BEARBHINZUFZUGELASSEN,BEARBZUGELASSEN,EINGABENOTWENDIG,ANSICHTSUCHEN,SUCHWERTAUSTABAKEY,SUCHWERTAUSSPALTEAKEY,WHERETABAKEY,WHERESPALTEAKEY,VERDECKENDURCHTABAKEY,VERDECKENDURCHSPALTEAKEY,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,ANSICHTDEFID,ANSICHTTABID,TABSPEIGID) values ('3','FX_TabDef_K','DatenBank','-1','-1','-1',null,null,null,null,null,null,null,null,null,null,null,'MW',to_date('04.07.18','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_AnsichtSpalten_K:DSID'),
(select dsid from FX_AnsichtDef_K where ansicht = 'FX_TabDef_K'),
(select dsid from FX_AnsichtTab_K where ansicht = 'FX_TabDef_K' and jointyp = 'x'),
(select FX_TabSpEig_K.dsid from FX_TabSpEig_K join FX_TabDef_K on FX_TabSpEig_K.TABDEFID = FX_TabDef_K.DSID where FX_TabDef_K.tabelle = 'FX_TabDef_K' and FX_TabSpEig_K.spalte = 'DatenBank'));

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_AnsichtSpalten_K:DSID';

Insert into FX_ANSICHTSPALTEN_K (INDEXGRID,TABAKEY,SPALTEAKEY,INGRIDANZEIGEN,INGRIDLADEN,FILTER,ANZAHLNACHKOMMASTELLEN,BEARBHINZUFZUGELASSEN,BEARBZUGELASSEN,EINGABENOTWENDIG,ANSICHTSUCHEN,SUCHWERTAUSTABAKEY,SUCHWERTAUSSPALTEAKEY,WHERETABAKEY,WHERESPALTEAKEY,VERDECKENDURCHTABAKEY,VERDECKENDURCHSPALTEAKEY,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,ANSICHTDEFID,ANSICHTTABID,TABSPEIGID) values ('13','FX_TabDef_K','EindeutigerSchluessel','-1','-1','-1',null,null,null,null,null,null,null,null,null,null,null,'MW',to_date('04.07.18','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_AnsichtSpalten_K:DSID'),
(select dsid from FX_AnsichtDef_K where ansicht = 'FX_TabDef_K'),
(select dsid from FX_AnsichtTab_K where ansicht = 'FX_TabDef_K' and jointyp = 'x'),
(select FX_TabSpEig_K.dsid from FX_TabSpEig_K join FX_TabDef_K on FX_TabSpEig_K.TABDEFID = FX_TabDef_K.DSID where FX_TabDef_K.tabelle = 'FX_TabDef_K' and FX_TabSpEig_K.spalte = 'EindeutigerSchluessel'));

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_AnsichtSpalten_K:DSID';

Insert into FX_ANSICHTSPALTEN_K (INDEXGRID,TABAKEY,SPALTEAKEY,INGRIDANZEIGEN,INGRIDLADEN,FILTER,ANZAHLNACHKOMMASTELLEN,BEARBHINZUFZUGELASSEN,BEARBZUGELASSEN,EINGABENOTWENDIG,ANSICHTSUCHEN,SUCHWERTAUSTABAKEY,SUCHWERTAUSSPALTEAKEY,WHERETABAKEY,WHERESPALTEAKEY,VERDECKENDURCHTABAKEY,VERDECKENDURCHSPALTEAKEY,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,ANSICHTDEFID,ANSICHTTABID,TABSPEIGID) values ('5','FX_TabDef_K','Lizenz','-1','-1','-1',null,null,null,null,null,null,null,null,null,null,null,'MW',to_date('04.07.18','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_AnsichtSpalten_K:DSID'),
(select dsid from FX_AnsichtDef_K where ansicht = 'FX_TabDef_K'),
(select dsid from FX_AnsichtTab_K where ansicht = 'FX_TabDef_K' and jointyp = 'x'),
(select FX_TabSpEig_K.dsid from FX_TabSpEig_K join FX_TabDef_K on FX_TabSpEig_K.TABDEFID = FX_TabDef_K.DSID where FX_TabDef_K.tabelle = 'FX_TabDef_K' and FX_TabSpEig_K.spalte = 'Lizenz'));

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_AnsichtSpalten_K:DSID';

Insert into FX_ANSICHTSPALTEN_K (INDEXGRID,TABAKEY,SPALTEAKEY,INGRIDANZEIGEN,INGRIDLADEN,FILTER,ANZAHLNACHKOMMASTELLEN,BEARBHINZUFZUGELASSEN,BEARBZUGELASSEN,EINGABENOTWENDIG,ANSICHTSUCHEN,SUCHWERTAUSTABAKEY,SUCHWERTAUSSPALTEAKEY,WHERETABAKEY,WHERESPALTEAKEY,VERDECKENDURCHTABAKEY,VERDECKENDURCHSPALTEAKEY,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,ANSICHTDEFID,ANSICHTTABID,TABSPEIGID) values ('24','FX_TabDef_K','System','-1','-1','-1',null,null,null,null,null,null,null,null,null,null,null,'MW',to_date('04.07.18','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_AnsichtSpalten_K:DSID'),
(select dsid from FX_AnsichtDef_K where ansicht = 'FX_TabDef_K'),
(select dsid from FX_AnsichtTab_K where ansicht = 'FX_TabDef_K' and jointyp = 'x'),
(select FX_TabSpEig_K.dsid from FX_TabSpEig_K join FX_TabDef_K on FX_TabSpEig_K.TABDEFID = FX_TabDef_K.DSID where FX_TabDef_K.tabelle = 'FX_TabDef_K' and FX_TabSpEig_K.spalte = 'System'));

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_AnsichtSpalten_K:DSID';

Insert into FX_ANSICHTSPALTEN_K (INDEXGRID,TABAKEY,SPALTEAKEY,INGRIDANZEIGEN,INGRIDLADEN,FILTER,ANZAHLNACHKOMMASTELLEN,BEARBHINZUFZUGELASSEN,BEARBZUGELASSEN,EINGABENOTWENDIG,ANSICHTSUCHEN,SUCHWERTAUSTABAKEY,SUCHWERTAUSSPALTEAKEY,WHERETABAKEY,WHERESPALTEAKEY,VERDECKENDURCHTABAKEY,VERDECKENDURCHSPALTEAKEY,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,ANSICHTDEFID,ANSICHTTABID,TABSPEIGID) values ('1','FX_TabDef_K','Tabelle','-1','-1','-1',null,null,null,null,null,null,null,null,null,null,null,'MW',to_date('04.07.18','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_AnsichtSpalten_K:DSID'),
(select dsid from FX_AnsichtDef_K where ansicht = 'FX_TabDef_K'),
(select dsid from FX_AnsichtTab_K where ansicht = 'FX_TabDef_K' and jointyp = 'x'),
(select FX_TabSpEig_K.dsid from FX_TabSpEig_K join FX_TabDef_K on FX_TabSpEig_K.TABDEFID = FX_TabDef_K.DSID where FX_TabDef_K.tabelle = 'FX_TabDef_K' and FX_TabSpEig_K.spalte = 'Tabelle'));

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_AnsichtSpalten_K:DSID';

Insert into FX_ANSICHTSPALTEN_K (INDEXGRID,TABAKEY,SPALTEAKEY,INGRIDANZEIGEN,INGRIDLADEN,FILTER,ANZAHLNACHKOMMASTELLEN,BEARBHINZUFZUGELASSEN,BEARBZUGELASSEN,EINGABENOTWENDIG,ANSICHTSUCHEN,SUCHWERTAUSTABAKEY,SUCHWERTAUSSPALTEAKEY,WHERETABAKEY,WHERESPALTEAKEY,VERDECKENDURCHTABAKEY,VERDECKENDURCHSPALTEAKEY,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,ANSICHTDEFID,ANSICHTTABID,TABSPEIGID) values ('10','FX_TabDef_K','ZeitTyp','-1','-1','-1',null,null,null,null,null,null,null,null,null,null,null,'MW',to_date('04.07.18','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_AnsichtSpalten_K:DSID'),
(select dsid from FX_AnsichtDef_K where ansicht = 'FX_TabDef_K'),
(select dsid from FX_AnsichtTab_K where ansicht = 'FX_TabDef_K' and jointyp = 'x'),
(select FX_TabSpEig_K.dsid from FX_TabSpEig_K join FX_TabDef_K on FX_TabSpEig_K.TABDEFID = FX_TabDef_K.DSID where FX_TabDef_K.tabelle = 'FX_TabDef_K' and FX_TabSpEig_K.spalte = 'ZeitTyp'));

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_AnsichtSpalten_K:DSID';

Insert into FX_ANSICHTSPALTEN_K (INDEXGRID,TABAKEY,SPALTEAKEY,INGRIDANZEIGEN,INGRIDLADEN,FILTER,ANZAHLNACHKOMMASTELLEN,BEARBHINZUFZUGELASSEN,BEARBZUGELASSEN,EINGABENOTWENDIG,ANSICHTSUCHEN,SUCHWERTAUSTABAKEY,SUCHWERTAUSSPALTEAKEY,WHERETABAKEY,WHERESPALTEAKEY,VERDECKENDURCHTABAKEY,VERDECKENDURCHSPALTEAKEY,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,ANSICHTDEFID,ANSICHTTABID,TABSPEIGID) values ('22','FX_TabDef_K','angelegtAm','-1','-1','-1',null,null,null,null,null,null,null,null,null,null,null,'MW',to_date('04.07.18','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_AnsichtSpalten_K:DSID'),
(select dsid from FX_AnsichtDef_K where ansicht = 'FX_TabDef_K'),
(select dsid from FX_AnsichtTab_K where ansicht = 'FX_TabDef_K' and jointyp = 'x'),
(select FX_TabSpEig_K.dsid from FX_TabSpEig_K join FX_TabDef_K on FX_TabSpEig_K.TABDEFID = FX_TabDef_K.DSID where FX_TabDef_K.tabelle = 'FX_TabDef_K' and FX_TabSpEig_K.spalte = 'angelegtAm'));

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_AnsichtSpalten_K:DSID';

Insert into FX_ANSICHTSPALTEN_K (INDEXGRID,TABAKEY,SPALTEAKEY,INGRIDANZEIGEN,INGRIDLADEN,FILTER,ANZAHLNACHKOMMASTELLEN,BEARBHINZUFZUGELASSEN,BEARBZUGELASSEN,EINGABENOTWENDIG,ANSICHTSUCHEN,SUCHWERTAUSTABAKEY,SUCHWERTAUSSPALTEAKEY,WHERETABAKEY,WHERESPALTEAKEY,VERDECKENDURCHTABAKEY,VERDECKENDURCHSPALTEAKEY,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,ANSICHTDEFID,ANSICHTTABID,TABSPEIGID) values ('21','FX_TabDef_K','angelegtVon','-1','-1','-1',null,null,null,null,null,null,null,null,null,null,null,'MW',to_date('04.07.18','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_AnsichtSpalten_K:DSID'),
(select dsid from FX_AnsichtDef_K where ansicht = 'FX_TabDef_K'),
(select dsid from FX_AnsichtTab_K where ansicht = 'FX_TabDef_K' and jointyp = 'x'),
(select FX_TabSpEig_K.dsid from FX_TabSpEig_K join FX_TabDef_K on FX_TabSpEig_K.TABDEFID = FX_TabDef_K.DSID where FX_TabDef_K.tabelle = 'FX_TabDef_K' and FX_TabSpEig_K.spalte = 'angelegtVon'));

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_AnsichtSpalten_K:DSID';

Insert into FX_ANSICHTSPALTEN_K (INDEXGRID,TABAKEY,SPALTEAKEY,INGRIDANZEIGEN,INGRIDLADEN,FILTER,ANZAHLNACHKOMMASTELLEN,BEARBHINZUFZUGELASSEN,BEARBZUGELASSEN,EINGABENOTWENDIG,ANSICHTSUCHEN,SUCHWERTAUSTABAKEY,SUCHWERTAUSSPALTEAKEY,WHERETABAKEY,WHERESPALTEAKEY,VERDECKENDURCHTABAKEY,VERDECKENDURCHSPALTEAKEY,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,ANSICHTDEFID,ANSICHTTABID,TABSPEIGID) values ('26','FX_TabDef_K','fullClassName','-1','-1','-1',null,'-1','-1',null,null,null,null,null,null,null,null,'MW#migration#',to_date('04.07.18','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_AnsichtSpalten_K:DSID'),
(select dsid from FX_AnsichtDef_K where ansicht = 'FX_TabDef_K'),
(select dsid from FX_AnsichtTab_K where ansicht = 'FX_TabDef_K' and jointyp = 'x'),
(select FX_TabSpEig_K.dsid from FX_TabSpEig_K join FX_TabDef_K on FX_TabSpEig_K.TABDEFID = FX_TabDef_K.DSID where FX_TabDef_K.tabelle = 'FX_TabDef_K' and FX_TabSpEig_K.spalte = 'fullClassName'));

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_AnsichtSpalten_K:DSID';

-- insert FX_AnsichtSpalten datasets for FX_TabSpEig_K
Insert into FX_ANSICHTSPALTEN_K (INDEXGRID,TABAKEY,SPALTEAKEY,INGRIDANZEIGEN,INGRIDLADEN,FILTER,ANZAHLNACHKOMMASTELLEN,BEARBHINZUFZUGELASSEN,BEARBZUGELASSEN,EINGABENOTWENDIG,ANSICHTSUCHEN,SUCHWERTAUSTABAKEY,SUCHWERTAUSSPALTEAKEY,WHERETABAKEY,WHERESPALTEAKEY,VERDECKENDURCHTABAKEY,VERDECKENDURCHSPALTEAKEY,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,ANSICHTDEFID,ANSICHTTABID,TABSPEIGID) values ('9','FX_TabSpEig_K','BearbErlaubt','-1','-1','-1',null,null,null,null,null,null,null,null,null,null,null,'MW',to_date('04.07.18','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_AnsichtSpalten_K:DSID'),
(select dsid from FX_AnsichtDef_K where ansicht = 'FX_TabSpEig_K'),
(select dsid from FX_AnsichtTab_K where ansicht = 'FX_TabSpEig_K' and jointyp = 'x'),
(select FX_TabSpEig_K.dsid from FX_TabSpEig_K join FX_TabDef_K on FX_TabSpEig_K.TABDEFID = FX_TabDef_K.DSID where FX_TabDef_K.tabelle = 'FX_TabSpEig_K' and FX_TabSpEig_K.spalte = 'BearbErlaubt'));

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_AnsichtSpalten_K:DSID';

Insert into FX_ANSICHTSPALTEN_K (INDEXGRID,TABAKEY,SPALTEAKEY,INGRIDANZEIGEN,INGRIDLADEN,FILTER,ANZAHLNACHKOMMASTELLEN,BEARBHINZUFZUGELASSEN,BEARBZUGELASSEN,EINGABENOTWENDIG,ANSICHTSUCHEN,SUCHWERTAUSTABAKEY,SUCHWERTAUSSPALTEAKEY,WHERETABAKEY,WHERESPALTEAKEY,VERDECKENDURCHTABAKEY,VERDECKENDURCHSPALTEAKEY,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,ANSICHTDEFID,ANSICHTTABID,TABSPEIGID) values ('23','FX_TabSpEig_K','DSID','-1','-1','-1',null,null,null,null,null,null,null,null,null,null,null,'MW',to_date('04.07.18','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_AnsichtSpalten_K:DSID'),
(select dsid from FX_AnsichtDef_K where ansicht = 'FX_TabSpEig_K'),
(select dsid from FX_AnsichtTab_K where ansicht = 'FX_TabSpEig_K' and jointyp = 'x'),
(select FX_TabSpEig_K.dsid from FX_TabSpEig_K join FX_TabDef_K on FX_TabSpEig_K.TABDEFID = FX_TabDef_K.DSID where FX_TabDef_K.tabelle = 'FX_TabSpEig_K' and FX_TabSpEig_K.spalte = 'DSID'));

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_AnsichtSpalten_K:DSID';

Insert into FX_ANSICHTSPALTEN_K (INDEXGRID,TABAKEY,SPALTEAKEY,INGRIDANZEIGEN,INGRIDLADEN,FILTER,ANZAHLNACHKOMMASTELLEN,BEARBHINZUFZUGELASSEN,BEARBZUGELASSEN,EINGABENOTWENDIG,ANSICHTSUCHEN,SUCHWERTAUSTABAKEY,SUCHWERTAUSSPALTEAKEY,WHERETABAKEY,WHERESPALTEAKEY,VERDECKENDURCHTABAKEY,VERDECKENDURCHSPALTEAKEY,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,ANSICHTDEFID,ANSICHTTABID,TABSPEIGID) values ('11','FX_TabSpEig_K','Defaultwert','-1','-1','-1',null,null,null,null,null,null,null,null,null,null,null,'MW',to_date('04.07.18','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_AnsichtSpalten_K:DSID'),
(select dsid from FX_AnsichtDef_K where ansicht = 'FX_TabSpEig_K'),
(select dsid from FX_AnsichtTab_K where ansicht = 'FX_TabSpEig_K' and jointyp = 'x'),
(select FX_TabSpEig_K.dsid from FX_TabSpEig_K join FX_TabDef_K on FX_TabSpEig_K.TABDEFID = FX_TabDef_K.DSID where FX_TabDef_K.tabelle = 'FX_TabSpEig_K' and FX_TabSpEig_K.spalte = 'Defaultwert'));

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_AnsichtSpalten_K:DSID';

Insert into FX_ANSICHTSPALTEN_K (INDEXGRID,TABAKEY,SPALTEAKEY,INGRIDANZEIGEN,INGRIDLADEN,FILTER,ANZAHLNACHKOMMASTELLEN,BEARBHINZUFZUGELASSEN,BEARBZUGELASSEN,EINGABENOTWENDIG,ANSICHTSUCHEN,SUCHWERTAUSTABAKEY,SUCHWERTAUSSPALTEAKEY,WHERETABAKEY,WHERESPALTEAKEY,VERDECKENDURCHTABAKEY,VERDECKENDURCHSPALTEAKEY,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,ANSICHTDEFID,ANSICHTTABID,TABSPEIGID) values ('7','FX_TabSpEig_K','Eindeutig','-1','-1','-1',null,null,null,null,null,null,null,null,null,null,null,'MW',to_date('04.07.18','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_AnsichtSpalten_K:DSID'),
(select dsid from FX_AnsichtDef_K where ansicht = 'FX_TabSpEig_K'),
(select dsid from FX_AnsichtTab_K where ansicht = 'FX_TabSpEig_K' and jointyp = 'x'),
(select FX_TabSpEig_K.dsid from FX_TabSpEig_K join FX_TabDef_K on FX_TabSpEig_K.TABDEFID = FX_TabDef_K.DSID where FX_TabDef_K.tabelle = 'FX_TabSpEig_K' and FX_TabSpEig_K.spalte = 'Eindeutig'));

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_AnsichtSpalten_K:DSID';

Insert into FX_ANSICHTSPALTEN_K (INDEXGRID,TABAKEY,SPALTEAKEY,INGRIDANZEIGEN,INGRIDLADEN,FILTER,ANZAHLNACHKOMMASTELLEN,BEARBHINZUFZUGELASSEN,BEARBZUGELASSEN,EINGABENOTWENDIG,ANSICHTSUCHEN,SUCHWERTAUSTABAKEY,SUCHWERTAUSSPALTEAKEY,WHERETABAKEY,WHERESPALTEAKEY,VERDECKENDURCHTABAKEY,VERDECKENDURCHSPALTEAKEY,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,ANSICHTDEFID,ANSICHTTABID,TABSPEIGID) values ('8','FX_TabSpEig_K','EingabeNotwendig','-1','-1','-1',null,null,null,null,null,null,null,null,null,null,null,'MW',to_date('04.07.18','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_AnsichtSpalten_K:DSID'),
(select dsid from FX_AnsichtDef_K where ansicht = 'FX_TabSpEig_K'),
(select dsid from FX_AnsichtTab_K where ansicht = 'FX_TabSpEig_K' and jointyp = 'x'),
(select FX_TabSpEig_K.dsid from FX_TabSpEig_K join FX_TabDef_K on FX_TabSpEig_K.TABDEFID = FX_TabDef_K.DSID where FX_TabDef_K.tabelle = 'FX_TabSpEig_K' and FX_TabSpEig_K.spalte = 'EingabeNotwendig'));

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_AnsichtSpalten_K:DSID';

Insert into FX_ANSICHTSPALTEN_K (INDEXGRID,TABAKEY,SPALTEAKEY,INGRIDANZEIGEN,INGRIDLADEN,FILTER,ANZAHLNACHKOMMASTELLEN,BEARBHINZUFZUGELASSEN,BEARBZUGELASSEN,EINGABENOTWENDIG,ANSICHTSUCHEN,SUCHWERTAUSTABAKEY,SUCHWERTAUSSPALTEAKEY,WHERETABAKEY,WHERESPALTEAKEY,VERDECKENDURCHTABAKEY,VERDECKENDURCHSPALTEAKEY,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,ANSICHTDEFID,ANSICHTTABID,TABSPEIGID) values ('14','FX_TabSpEig_K','Maximum','-1','-1','-1',null,null,null,null,null,null,null,null,null,null,null,'MW',to_date('04.07.18','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_AnsichtSpalten_K:DSID'),
(select dsid from FX_AnsichtDef_K where ansicht = 'FX_TabSpEig_K'),
(select dsid from FX_AnsichtTab_K where ansicht = 'FX_TabSpEig_K' and jointyp = 'x'),
(select FX_TabSpEig_K.dsid from FX_TabSpEig_K join FX_TabDef_K on FX_TabSpEig_K.TABDEFID = FX_TabDef_K.DSID where FX_TabDef_K.tabelle = 'FX_TabSpEig_K' and FX_TabSpEig_K.spalte = 'Maximum'));

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_AnsichtSpalten_K:DSID';

Insert into FX_ANSICHTSPALTEN_K (INDEXGRID,TABAKEY,SPALTEAKEY,INGRIDANZEIGEN,INGRIDLADEN,FILTER,ANZAHLNACHKOMMASTELLEN,BEARBHINZUFZUGELASSEN,BEARBZUGELASSEN,EINGABENOTWENDIG,ANSICHTSUCHEN,SUCHWERTAUSTABAKEY,SUCHWERTAUSSPALTEAKEY,WHERETABAKEY,WHERESPALTEAKEY,VERDECKENDURCHTABAKEY,VERDECKENDURCHSPALTEAKEY,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,ANSICHTDEFID,ANSICHTTABID,TABSPEIGID) values ('13','FX_TabSpEig_K','Minimum','-1','-1','-1',null,null,null,null,null,null,null,null,null,null,null,'MW',to_date('04.07.18','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_AnsichtSpalten_K:DSID'),
(select dsid from FX_AnsichtDef_K where ansicht = 'FX_TabSpEig_K'),
(select dsid from FX_AnsichtTab_K where ansicht = 'FX_TabSpEig_K' and jointyp = 'x'),
(select FX_TabSpEig_K.dsid from FX_TabSpEig_K join FX_TabDef_K on FX_TabSpEig_K.TABDEFID = FX_TabDef_K.DSID where FX_TabDef_K.tabelle = 'FX_TabSpEig_K' and FX_TabSpEig_K.spalte = 'Minimum'));

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_AnsichtSpalten_K:DSID';

Insert into FX_ANSICHTSPALTEN_K (INDEXGRID,TABAKEY,SPALTEAKEY,INGRIDANZEIGEN,INGRIDLADEN,FILTER,ANZAHLNACHKOMMASTELLEN,BEARBHINZUFZUGELASSEN,BEARBZUGELASSEN,EINGABENOTWENDIG,ANSICHTSUCHEN,SUCHWERTAUSTABAKEY,SUCHWERTAUSSPALTEAKEY,WHERETABAKEY,WHERESPALTEAKEY,VERDECKENDURCHTABAKEY,VERDECKENDURCHSPALTEAKEY,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,ANSICHTDEFID,ANSICHTTABID,TABSPEIGID) values ('6','FX_TabSpEig_K','PS','-1','-1','-1',null,null,null,null,null,null,null,null,null,null,null,'MW',to_date('04.07.18','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_AnsichtSpalten_K:DSID'),
(select dsid from FX_AnsichtDef_K where ansicht = 'FX_TabSpEig_K'),
(select dsid from FX_AnsichtTab_K where ansicht = 'FX_TabSpEig_K' and jointyp = 'x'),
(select FX_TabSpEig_K.dsid from FX_TabSpEig_K join FX_TabDef_K on FX_TabSpEig_K.TABDEFID = FX_TabDef_K.DSID where FX_TabDef_K.tabelle = 'FX_TabSpEig_K' and FX_TabSpEig_K.spalte = 'PS'));

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_AnsichtSpalten_K:DSID';

Insert into FX_ANSICHTSPALTEN_K (INDEXGRID,TABAKEY,SPALTEAKEY,INGRIDANZEIGEN,INGRIDLADEN,FILTER,ANZAHLNACHKOMMASTELLEN,BEARBHINZUFZUGELASSEN,BEARBZUGELASSEN,EINGABENOTWENDIG,ANSICHTSUCHEN,SUCHWERTAUSTABAKEY,SUCHWERTAUSSPALTEAKEY,WHERETABAKEY,WHERESPALTEAKEY,VERDECKENDURCHTABAKEY,VERDECKENDURCHSPALTEAKEY,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,ANSICHTDEFID,ANSICHTTABID,TABSPEIGID) values ('2','FX_TabSpEig_K','Reihenfolge','-1','-1','-1',null,null,null,null,null,null,null,null,null,null,null,'MW',to_date('04.07.18','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_AnsichtSpalten_K:DSID'),
(select dsid from FX_AnsichtDef_K where ansicht = 'FX_TabSpEig_K'),
(select dsid from FX_AnsichtTab_K where ansicht = 'FX_TabSpEig_K' and jointyp = 'x'),
(select FX_TabSpEig_K.dsid from FX_TabSpEig_K join FX_TabDef_K on FX_TabSpEig_K.TABDEFID = FX_TabDef_K.DSID where FX_TabDef_K.tabelle = 'FX_TabSpEig_K' and FX_TabSpEig_K.spalte = 'Reihenfolge'));

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_AnsichtSpalten_K:DSID';

Insert into FX_ANSICHTSPALTEN_K (INDEXGRID,TABAKEY,SPALTEAKEY,INGRIDANZEIGEN,INGRIDLADEN,FILTER,ANZAHLNACHKOMMASTELLEN,BEARBHINZUFZUGELASSEN,BEARBZUGELASSEN,EINGABENOTWENDIG,ANSICHTSUCHEN,SUCHWERTAUSTABAKEY,SUCHWERTAUSSPALTEAKEY,WHERETABAKEY,WHERESPALTEAKEY,VERDECKENDURCHTABAKEY,VERDECKENDURCHSPALTEAKEY,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,ANSICHTDEFID,ANSICHTTABID,TABSPEIGID) values ('3','FX_TabSpEig_K','Spalte','-1','-1','-1',null,null,null,null,null,null,null,null,null,null,null,'MW',to_date('04.07.18','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_AnsichtSpalten_K:DSID'),
(select dsid from FX_AnsichtDef_K where ansicht = 'FX_TabSpEig_K'),
(select dsid from FX_AnsichtTab_K where ansicht = 'FX_TabSpEig_K' and jointyp = 'x'),
(select FX_TabSpEig_K.dsid from FX_TabSpEig_K join FX_TabDef_K on FX_TabSpEig_K.TABDEFID = FX_TabDef_K.DSID where FX_TabDef_K.tabelle = 'FX_TabSpEig_K' and FX_TabSpEig_K.spalte = 'Spalte'));

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_AnsichtSpalten_K:DSID';

Insert into FX_ANSICHTSPALTEN_K (INDEXGRID,TABAKEY,SPALTEAKEY,INGRIDANZEIGEN,INGRIDLADEN,FILTER,ANZAHLNACHKOMMASTELLEN,BEARBHINZUFZUGELASSEN,BEARBZUGELASSEN,EINGABENOTWENDIG,ANSICHTSUCHEN,SUCHWERTAUSTABAKEY,SUCHWERTAUSSPALTEAKEY,WHERETABAKEY,WHERESPALTEAKEY,VERDECKENDURCHTABAKEY,VERDECKENDURCHSPALTEAKEY,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,ANSICHTDEFID,ANSICHTTABID,TABSPEIGID) values ('17','FX_TabSpEig_K','SpaltenTyp','-1','-1','-1',null,null,null,null,null,null,null,null,null,null,null,'MW',to_date('04.07.18','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_AnsichtSpalten_K:DSID'),
(select dsid from FX_AnsichtDef_K where ansicht = 'FX_TabSpEig_K'),
(select dsid from FX_AnsichtTab_K where ansicht = 'FX_TabSpEig_K' and jointyp = 'x'),
(select FX_TabSpEig_K.dsid from FX_TabSpEig_K join FX_TabDef_K on FX_TabSpEig_K.TABDEFID = FX_TabDef_K.DSID where FX_TabDef_K.tabelle = 'FX_TabSpEig_K' and FX_TabSpEig_K.spalte = 'SpaltenTyp'));

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_AnsichtSpalten_K:DSID';

Insert into FX_ANSICHTSPALTEN_K (INDEXGRID,TABAKEY,SPALTEAKEY,INGRIDANZEIGEN,INGRIDLADEN,FILTER,ANZAHLNACHKOMMASTELLEN,BEARBHINZUFZUGELASSEN,BEARBZUGELASSEN,EINGABENOTWENDIG,ANSICHTSUCHEN,SUCHWERTAUSTABAKEY,SUCHWERTAUSSPALTEAKEY,WHERETABAKEY,WHERESPALTEAKEY,VERDECKENDURCHTABAKEY,VERDECKENDURCHSPALTEAKEY,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,ANSICHTDEFID,ANSICHTTABID,TABSPEIGID) values ('4','FX_TabSpEig_K','Spaltenkopf','-1','-1','-1',null,null,null,null,null,null,null,null,null,null,null,'MW',to_date('04.07.18','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_AnsichtSpalten_K:DSID'),
(select dsid from FX_AnsichtDef_K where ansicht = 'FX_TabSpEig_K'),
(select dsid from FX_AnsichtTab_K where ansicht = 'FX_TabSpEig_K' and jointyp = 'x'),
(select FX_TabSpEig_K.dsid from FX_TabSpEig_K join FX_TabDef_K on FX_TabSpEig_K.TABDEFID = FX_TabDef_K.DSID where FX_TabDef_K.tabelle = 'FX_TabSpEig_K' and FX_TabSpEig_K.spalte = 'Spaltenkopf'));

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_AnsichtSpalten_K:DSID';

Insert into FX_ANSICHTSPALTEN_K (INDEXGRID,TABAKEY,SPALTEAKEY,INGRIDANZEIGEN,INGRIDLADEN,FILTER,ANZAHLNACHKOMMASTELLEN,BEARBHINZUFZUGELASSEN,BEARBZUGELASSEN,EINGABENOTWENDIG,ANSICHTSUCHEN,SUCHWERTAUSTABAKEY,SUCHWERTAUSSPALTEAKEY,WHERETABAKEY,WHERESPALTEAKEY,VERDECKENDURCHTABAKEY,VERDECKENDURCHSPALTEAKEY,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,ANSICHTDEFID,ANSICHTTABID,TABSPEIGID) values ('27','FX_TabSpEig_K','System','-1','-1','-1',null,null,null,null,null,null,null,null,null,null,null,'MW',to_date('04.07.18','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_AnsichtSpalten_K:DSID'),
(select dsid from FX_AnsichtDef_K where ansicht = 'FX_TabSpEig_K'),
(select dsid from FX_AnsichtTab_K where ansicht = 'FX_TabSpEig_K' and jointyp = 'x'),
(select FX_TabSpEig_K.dsid from FX_TabSpEig_K join FX_TabDef_K on FX_TabSpEig_K.TABDEFID = FX_TabDef_K.DSID where FX_TabDef_K.tabelle = 'FX_TabSpEig_K' and FX_TabSpEig_K.spalte = 'System'));

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_AnsichtSpalten_K:DSID';

Insert into FX_ANSICHTSPALTEN_K (INDEXGRID,TABAKEY,SPALTEAKEY,INGRIDANZEIGEN,INGRIDLADEN,FILTER,ANZAHLNACHKOMMASTELLEN,BEARBHINZUFZUGELASSEN,BEARBZUGELASSEN,EINGABENOTWENDIG,ANSICHTSUCHEN,SUCHWERTAUSTABAKEY,SUCHWERTAUSSPALTEAKEY,WHERETABAKEY,WHERESPALTEAKEY,VERDECKENDURCHTABAKEY,VERDECKENDURCHSPALTEAKEY,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,ANSICHTDEFID,ANSICHTTABID,TABSPEIGID) values ('10','FX_TabSpEig_K','SystemWert','-1','-1','-1',null,null,null,null,null,null,null,null,null,null,null,'MW',to_date('04.07.18','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_AnsichtSpalten_K:DSID'),
(select dsid from FX_AnsichtDef_K where ansicht = 'FX_TabSpEig_K'),
(select dsid from FX_AnsichtTab_K where ansicht = 'FX_TabSpEig_K' and jointyp = 'x'),
(select FX_TabSpEig_K.dsid from FX_TabSpEig_K join FX_TabDef_K on FX_TabSpEig_K.TABDEFID = FX_TabDef_K.DSID where FX_TabDef_K.tabelle = 'FX_TabSpEig_K' and FX_TabSpEig_K.spalte = 'SystemWert'));

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_AnsichtSpalten_K:DSID';

Insert into FX_ANSICHTSPALTEN_K (INDEXGRID,TABAKEY,SPALTEAKEY,INGRIDANZEIGEN,INGRIDLADEN,FILTER,ANZAHLNACHKOMMASTELLEN,BEARBHINZUFZUGELASSEN,BEARBZUGELASSEN,EINGABENOTWENDIG,ANSICHTSUCHEN,SUCHWERTAUSTABAKEY,SUCHWERTAUSSPALTEAKEY,WHERETABAKEY,WHERESPALTEAKEY,VERDECKENDURCHTABAKEY,VERDECKENDURCHSPALTEAKEY,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,ANSICHTDEFID,ANSICHTTABID,TABSPEIGID) values ('28','FX_TabSpEig_K','TabDefId',null,'-1','-1',null,'-1',null,null,'FX_TabDef_K','FX_TabDef_K','Tabelle',null,null,'FX_TabDef_K','Tabelle','MW#migration#',to_date('04.07.18','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_AnsichtSpalten_K:DSID'),
(select dsid from FX_AnsichtDef_K where ansicht = 'FX_TabSpEig_K'),
(select dsid from FX_AnsichtTab_K where ansicht = 'FX_TabSpEig_K' and jointyp = 'x'),
(select FX_TabSpEig_K.dsid from FX_TabSpEig_K join FX_TabDef_K on FX_TabSpEig_K.TABDEFID = FX_TabDef_K.DSID where FX_TabDef_K.tabelle = 'FX_TabSpEig_K' and FX_TabSpEig_K.spalte = 'TabDefId'));

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_AnsichtSpalten_K:DSID';

Insert into FX_ANSICHTSPALTEN_K (INDEXGRID,TABAKEY,SPALTEAKEY,INGRIDANZEIGEN,INGRIDLADEN,FILTER,ANZAHLNACHKOMMASTELLEN,BEARBHINZUFZUGELASSEN,BEARBZUGELASSEN,EINGABENOTWENDIG,ANSICHTSUCHEN,SUCHWERTAUSTABAKEY,SUCHWERTAUSSPALTEAKEY,WHERETABAKEY,WHERESPALTEAKEY,VERDECKENDURCHTABAKEY,VERDECKENDURCHSPALTEAKEY,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,ANSICHTDEFID,ANSICHTTABID,TABSPEIGID) values ('1','FX_TabSpEig_K','Tabelle','-1','-1','-1',null,null,null,null,null,null,null,null,null,null,null,'MW',to_date('04.07.18','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_AnsichtSpalten_K:DSID'),
(select dsid from FX_AnsichtDef_K where ansicht = 'FX_TabSpEig_K'),
(select dsid from FX_AnsichtTab_K where ansicht = 'FX_TabSpEig_K' and jointyp = 'x'),
(select FX_TabSpEig_K.dsid from FX_TabSpEig_K join FX_TabDef_K on FX_TabSpEig_K.TABDEFID = FX_TabDef_K.DSID where FX_TabDef_K.tabelle = 'FX_TabSpEig_K' and FX_TabSpEig_K.spalte = 'Tabelle'));

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_AnsichtSpalten_K:DSID';

Insert into FX_ANSICHTSPALTEN_K (INDEXGRID,TABAKEY,SPALTEAKEY,INGRIDANZEIGEN,INGRIDLADEN,FILTER,ANZAHLNACHKOMMASTELLEN,BEARBHINZUFZUGELASSEN,BEARBZUGELASSEN,EINGABENOTWENDIG,ANSICHTSUCHEN,SUCHWERTAUSTABAKEY,SUCHWERTAUSSPALTEAKEY,WHERETABAKEY,WHERESPALTEAKEY,VERDECKENDURCHTABAKEY,VERDECKENDURCHSPALTEAKEY,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,ANSICHTDEFID,ANSICHTTABID,TABSPEIGID) values ('25','FX_TabSpEig_K','angelegtAm','-1','-1','-1',null,null,null,null,null,null,null,null,null,null,null,'MW',to_date('04.07.18','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_AnsichtSpalten_K:DSID'),
(select dsid from FX_AnsichtDef_K where ansicht = 'FX_TabSpEig_K'),
(select dsid from FX_AnsichtTab_K where ansicht = 'FX_TabSpEig_K' and jointyp = 'x'),
(select FX_TabSpEig_K.dsid from FX_TabSpEig_K join FX_TabDef_K on FX_TabSpEig_K.TABDEFID = FX_TabDef_K.DSID where FX_TabDef_K.tabelle = 'FX_TabSpEig_K' and FX_TabSpEig_K.spalte = 'angelegtAm'));

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_AnsichtSpalten_K:DSID';

Insert into FX_ANSICHTSPALTEN_K (INDEXGRID,TABAKEY,SPALTEAKEY,INGRIDANZEIGEN,INGRIDLADEN,FILTER,ANZAHLNACHKOMMASTELLEN,BEARBHINZUFZUGELASSEN,BEARBZUGELASSEN,EINGABENOTWENDIG,ANSICHTSUCHEN,SUCHWERTAUSTABAKEY,SUCHWERTAUSSPALTEAKEY,WHERETABAKEY,WHERESPALTEAKEY,VERDECKENDURCHTABAKEY,VERDECKENDURCHSPALTEAKEY,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,ANSICHTDEFID,ANSICHTTABID,TABSPEIGID) values ('24','FX_TabSpEig_K','angelegtVon','-1','-1','-1',null,null,null,null,null,null,null,null,null,null,null,'MW',to_date('04.07.18','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_AnsichtSpalten_K:DSID'),
(select dsid from FX_AnsichtDef_K where ansicht = 'FX_TabSpEig_K'),
(select dsid from FX_AnsichtTab_K where ansicht = 'FX_TabSpEig_K' and jointyp = 'x'),
(select FX_TabSpEig_K.dsid from FX_TabSpEig_K join FX_TabDef_K on FX_TabSpEig_K.TABDEFID = FX_TabDef_K.DSID where FX_TabDef_K.tabelle = 'FX_TabSpEig_K' and FX_TabSpEig_K.spalte = 'angelegtVon'));

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_AnsichtSpalten_K:DSID';

-- insert FX_AnsichtSpalten datasets for FX_AnsichtDef_k
Insert into FX_ANSICHTSPALTEN_K (INDEXGRID,TABAKEY,SPALTEAKEY,INGRIDANZEIGEN,INGRIDLADEN,FILTER,ANZAHLNACHKOMMASTELLEN,BEARBHINZUFZUGELASSEN,BEARBZUGELASSEN,EINGABENOTWENDIG,ANSICHTSUCHEN,SUCHWERTAUSTABAKEY,SUCHWERTAUSSPALTEAKEY,WHERETABAKEY,WHERESPALTEAKEY,VERDECKENDURCHTABAKEY,VERDECKENDURCHSPALTEAKEY,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,ANSICHTDEFID,ANSICHTTABID,TABSPEIGID) values ('30','FX_AnsichtDef_K','AngelegtAm','-1','-1','-1',null,null,null,null,null,null,null,null,null,null,null,'MW',to_date('04.07.18','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_AnsichtSpalten_K:DSID'),
(select dsid from FX_AnsichtDef_K where ansicht = 'FX_AnsichtDef_K'),
(select dsid from FX_AnsichtTab_K where ansicht = 'FX_AnsichtDef_K' and jointyp = 'x'),
(select FX_TabSpEig_K.dsid from FX_TabSpEig_K join FX_TabDef_K on FX_TabSpEig_K.TABDEFID = FX_TabDef_K.DSID where FX_TabDef_K.tabelle = 'FX_AnsichtDef_K' and FX_TabSpEig_K.spalte = 'AngelegtAm'));

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_AnsichtSpalten_K:DSID';

Insert into FX_ANSICHTSPALTEN_K (INDEXGRID,TABAKEY,SPALTEAKEY,INGRIDANZEIGEN,INGRIDLADEN,FILTER,ANZAHLNACHKOMMASTELLEN,BEARBHINZUFZUGELASSEN,BEARBZUGELASSEN,EINGABENOTWENDIG,ANSICHTSUCHEN,SUCHWERTAUSTABAKEY,SUCHWERTAUSSPALTEAKEY,WHERETABAKEY,WHERESPALTEAKEY,VERDECKENDURCHTABAKEY,VERDECKENDURCHSPALTEAKEY,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,ANSICHTDEFID,ANSICHTTABID,TABSPEIGID) values ('29','FX_AnsichtDef_K','AngelegtVon','-1','-1','-1',null,null,null,null,null,null,null,null,null,null,null,'MW',to_date('04.07.18','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_AnsichtSpalten_K:DSID'),
(select dsid from FX_AnsichtDef_K where ansicht = 'FX_AnsichtDef_K'),
(select dsid from FX_AnsichtTab_K where ansicht = 'FX_AnsichtDef_K' and jointyp = 'x'),
(select FX_TabSpEig_K.dsid from FX_TabSpEig_K join FX_TabDef_K on FX_TabSpEig_K.TABDEFID = FX_TabDef_K.DSID where FX_TabDef_K.tabelle = 'FX_AnsichtDef_K' and FX_TabSpEig_K.spalte = 'AngelegtVon'));

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_AnsichtSpalten_K:DSID';

Insert into FX_ANSICHTSPALTEN_K (INDEXGRID,TABAKEY,SPALTEAKEY,INGRIDANZEIGEN,INGRIDLADEN,FILTER,ANZAHLNACHKOMMASTELLEN,BEARBHINZUFZUGELASSEN,BEARBZUGELASSEN,EINGABENOTWENDIG,ANSICHTSUCHEN,SUCHWERTAUSTABAKEY,SUCHWERTAUSSPALTEAKEY,WHERETABAKEY,WHERESPALTEAKEY,VERDECKENDURCHTABAKEY,VERDECKENDURCHSPALTEAKEY,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,ANSICHTDEFID,ANSICHTTABID,TABSPEIGID) values ('1','FX_AnsichtDef_K','Ansicht','-1','-1','-1',null,null,null,null,null,null,null,null,null,null,null,'MW',to_date('04.07.18','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_AnsichtSpalten_K:DSID'),
(select dsid from FX_AnsichtDef_K where ansicht = 'FX_AnsichtDef_K'),
(select dsid from FX_AnsichtTab_K where ansicht = 'FX_AnsichtDef_K' and jointyp = 'x'),
(select FX_TabSpEig_K.dsid from FX_TabSpEig_K join FX_TabDef_K on FX_TabSpEig_K.TABDEFID = FX_TabDef_K.DSID where FX_TabDef_K.tabelle = 'FX_AnsichtDef_K' and FX_TabSpEig_K.spalte = 'Ansicht'));

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_AnsichtSpalten_K:DSID';

Insert into FX_ANSICHTSPALTEN_K (INDEXGRID,TABAKEY,SPALTEAKEY,INGRIDANZEIGEN,INGRIDLADEN,FILTER,ANZAHLNACHKOMMASTELLEN,BEARBHINZUFZUGELASSEN,BEARBZUGELASSEN,EINGABENOTWENDIG,ANSICHTSUCHEN,SUCHWERTAUSTABAKEY,SUCHWERTAUSSPALTEAKEY,WHERETABAKEY,WHERESPALTEAKEY,VERDECKENDURCHTABAKEY,VERDECKENDURCHSPALTEAKEY,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,ANSICHTDEFID,ANSICHTTABID,TABSPEIGID) values ('9','FX_AnsichtDef_K','Bearbeiten','-1','-1','-1',null,null,null,null,null,null,null,null,null,null,null,'MW',to_date('04.07.18','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_AnsichtSpalten_K:DSID'),
(select dsid from FX_AnsichtDef_K where ansicht = 'FX_AnsichtDef_K'),
(select dsid from FX_AnsichtTab_K where ansicht = 'FX_AnsichtDef_K' and jointyp = 'x'),
(select FX_TabSpEig_K.dsid from FX_TabSpEig_K join FX_TabDef_K on FX_TabSpEig_K.TABDEFID = FX_TabDef_K.DSID where FX_TabDef_K.tabelle = 'FX_AnsichtDef_K' and FX_TabSpEig_K.spalte = 'Bearbeiten'));

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_AnsichtSpalten_K:DSID';

Insert into FX_ANSICHTSPALTEN_K (INDEXGRID,TABAKEY,SPALTEAKEY,INGRIDANZEIGEN,INGRIDLADEN,FILTER,ANZAHLNACHKOMMASTELLEN,BEARBHINZUFZUGELASSEN,BEARBZUGELASSEN,EINGABENOTWENDIG,ANSICHTSUCHEN,SUCHWERTAUSTABAKEY,SUCHWERTAUSSPALTEAKEY,WHERETABAKEY,WHERESPALTEAKEY,VERDECKENDURCHTABAKEY,VERDECKENDURCHSPALTEAKEY,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,ANSICHTDEFID,ANSICHTTABID,TABSPEIGID) values ('4','FX_AnsichtDef_K','Bezeichnung','-1','-1','-1',null,null,null,null,null,null,null,null,null,null,null,'MW',to_date('04.07.18','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_AnsichtSpalten_K:DSID'),
(select dsid from FX_AnsichtDef_K where ansicht = 'FX_AnsichtDef_K'),
(select dsid from FX_AnsichtTab_K where ansicht = 'FX_AnsichtDef_K' and jointyp = 'x'),
(select FX_TabSpEig_K.dsid from FX_TabSpEig_K join FX_TabDef_K on FX_TabSpEig_K.TABDEFID = FX_TabDef_K.DSID where FX_TabDef_K.tabelle = 'FX_AnsichtDef_K' and FX_TabSpEig_K.spalte = 'Bezeichnung'));

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_AnsichtSpalten_K:DSID';

Insert into FX_ANSICHTSPALTEN_K (INDEXGRID,TABAKEY,SPALTEAKEY,INGRIDANZEIGEN,INGRIDLADEN,FILTER,ANZAHLNACHKOMMASTELLEN,BEARBHINZUFZUGELASSEN,BEARBZUGELASSEN,EINGABENOTWENDIG,ANSICHTSUCHEN,SUCHWERTAUSTABAKEY,SUCHWERTAUSSPALTEAKEY,WHERETABAKEY,WHERESPALTEAKEY,VERDECKENDURCHTABAKEY,VERDECKENDURCHSPALTEAKEY,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,ANSICHTDEFID,ANSICHTTABID,TABSPEIGID) values ('28','FX_AnsichtDef_K','DSID','-1','-1','-1',null,null,null,null,null,null,null,null,null,null,null,'MW',to_date('04.07.18','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_AnsichtSpalten_K:DSID'),
(select dsid from FX_AnsichtDef_K where ansicht = 'FX_AnsichtDef_K'),
(select dsid from FX_AnsichtTab_K where ansicht = 'FX_AnsichtDef_K' and jointyp = 'x'),
(select FX_TabSpEig_K.dsid from FX_TabSpEig_K join FX_TabDef_K on FX_TabSpEig_K.TABDEFID = FX_TabDef_K.DSID where FX_TabDef_K.tabelle = 'FX_AnsichtDef_K' and FX_TabSpEig_K.spalte = 'DSID'));

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_AnsichtSpalten_K:DSID';

Insert into FX_ANSICHTSPALTEN_K (INDEXGRID,TABAKEY,SPALTEAKEY,INGRIDANZEIGEN,INGRIDLADEN,FILTER,ANZAHLNACHKOMMASTELLEN,BEARBHINZUFZUGELASSEN,BEARBZUGELASSEN,EINGABENOTWENDIG,ANSICHTSUCHEN,SUCHWERTAUSTABAKEY,SUCHWERTAUSSPALTEAKEY,WHERETABAKEY,WHERESPALTEAKEY,VERDECKENDURCHTABAKEY,VERDECKENDURCHSPALTEAKEY,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,ANSICHTDEFID,ANSICHTTABID,TABSPEIGID) values ('10','FX_AnsichtDef_K','Entfernen','-1','-1','-1',null,null,null,null,null,null,null,null,null,null,null,'MW',to_date('04.07.18','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_AnsichtSpalten_K:DSID'),
(select dsid from FX_AnsichtDef_K where ansicht = 'FX_AnsichtDef_K'),
(select dsid from FX_AnsichtTab_K where ansicht = 'FX_AnsichtDef_K' and jointyp = 'x'),
(select FX_TabSpEig_K.dsid from FX_TabSpEig_K join FX_TabDef_K on FX_TabSpEig_K.TABDEFID = FX_TabDef_K.DSID where FX_TabDef_K.tabelle = 'FX_AnsichtDef_K' and FX_TabSpEig_K.spalte = 'Entfernen'));

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_AnsichtSpalten_K:DSID';

Insert into FX_ANSICHTSPALTEN_K (INDEXGRID,TABAKEY,SPALTEAKEY,INGRIDANZEIGEN,INGRIDLADEN,FILTER,ANZAHLNACHKOMMASTELLEN,BEARBHINZUFZUGELASSEN,BEARBZUGELASSEN,EINGABENOTWENDIG,ANSICHTSUCHEN,SUCHWERTAUSTABAKEY,SUCHWERTAUSSPALTEAKEY,WHERETABAKEY,WHERESPALTEAKEY,VERDECKENDURCHTABAKEY,VERDECKENDURCHSPALTEAKEY,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,ANSICHTDEFID,ANSICHTTABID,TABSPEIGID) values ('15','FX_AnsichtDef_K','Filter','-1','-1','-1',null,null,null,null,null,null,null,null,null,null,null,'MW',to_date('04.07.18','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_AnsichtSpalten_K:DSID'),
(select dsid from FX_AnsichtDef_K where ansicht = 'FX_AnsichtDef_K'),
(select dsid from FX_AnsichtTab_K where ansicht = 'FX_AnsichtDef_K' and jointyp = 'x'),
(select FX_TabSpEig_K.dsid from FX_TabSpEig_K join FX_TabDef_K on FX_TabSpEig_K.TABDEFID = FX_TabDef_K.DSID where FX_TabDef_K.tabelle = 'FX_AnsichtDef_K' and FX_TabSpEig_K.spalte = 'Filter'));

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_AnsichtSpalten_K:DSID';

Insert into FX_ANSICHTSPALTEN_K (INDEXGRID,TABAKEY,SPALTEAKEY,INGRIDANZEIGEN,INGRIDLADEN,FILTER,ANZAHLNACHKOMMASTELLEN,BEARBHINZUFZUGELASSEN,BEARBZUGELASSEN,EINGABENOTWENDIG,ANSICHTSUCHEN,SUCHWERTAUSTABAKEY,SUCHWERTAUSSPALTEAKEY,WHERETABAKEY,WHERESPALTEAKEY,VERDECKENDURCHTABAKEY,VERDECKENDURCHSPALTEAKEY,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,ANSICHTDEFID,ANSICHTTABID,TABSPEIGID) values ('7','FX_AnsichtDef_K','Hinzufuegen','-1','-1','-1',null,null,null,null,null,null,null,null,null,null,null,'MW',to_date('04.07.18','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_AnsichtSpalten_K:DSID'),
(select dsid from FX_AnsichtDef_K where ansicht = 'FX_AnsichtDef_K'),
(select dsid from FX_AnsichtTab_K where ansicht = 'FX_AnsichtDef_K' and jointyp = 'x'),
(select FX_TabSpEig_K.dsid from FX_TabSpEig_K join FX_TabDef_K on FX_TabSpEig_K.TABDEFID = FX_TabDef_K.DSID where FX_TabDef_K.tabelle = 'FX_AnsichtDef_K' and FX_TabSpEig_K.spalte = 'Hinzufuegen'));

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_AnsichtSpalten_K:DSID';


Insert into FX_ANSICHTSPALTEN_K (INDEXGRID,TABAKEY,SPALTEAKEY,INGRIDANZEIGEN,INGRIDLADEN,FILTER,ANZAHLNACHKOMMASTELLEN,BEARBHINZUFZUGELASSEN,BEARBZUGELASSEN,EINGABENOTWENDIG,ANSICHTSUCHEN,SUCHWERTAUSTABAKEY,SUCHWERTAUSSPALTEAKEY,WHERETABAKEY,WHERESPALTEAKEY,VERDECKENDURCHTABAKEY,VERDECKENDURCHSPALTEAKEY,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,ANSICHTDEFID,ANSICHTTABID,TABSPEIGID) values ('14','FX_AnsichtDef_K','Lesen','-1','-1','-1',null,null,null,null,null,null,null,null,null,null,null,'MW',to_date('04.07.18','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_AnsichtSpalten_K:DSID'),
(select dsid from FX_AnsichtDef_K where ansicht = 'FX_AnsichtDef_K'),
(select dsid from FX_AnsichtTab_K where ansicht = 'FX_AnsichtDef_K' and jointyp = 'x'),
(select FX_TabSpEig_K.dsid from FX_TabSpEig_K join FX_TabDef_K on FX_TabSpEig_K.TABDEFID = FX_TabDef_K.DSID where FX_TabDef_K.tabelle = 'FX_AnsichtDef_K' and FX_TabSpEig_K.spalte = 'Lesen'));

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_AnsichtSpalten_K:DSID';

Insert into FX_ANSICHTSPALTEN_K (INDEXGRID,TABAKEY,SPALTEAKEY,INGRIDANZEIGEN,INGRIDLADEN,FILTER,ANZAHLNACHKOMMASTELLEN,BEARBHINZUFZUGELASSEN,BEARBZUGELASSEN,EINGABENOTWENDIG,ANSICHTSUCHEN,SUCHWERTAUSTABAKEY,SUCHWERTAUSSPALTEAKEY,WHERETABAKEY,WHERESPALTEAKEY,VERDECKENDURCHTABAKEY,VERDECKENDURCHSPALTEAKEY,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,ANSICHTDEFID,ANSICHTTABID,TABSPEIGID) values ('16','FX_AnsichtDef_K','Sortieren','-1','-1','-1',null,null,null,null,null,null,null,null,null,null,null,'MW',to_date('04.07.18','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_AnsichtSpalten_K:DSID'),
(select dsid from FX_AnsichtDef_K where ansicht = 'FX_AnsichtDef_K'),
(select dsid from FX_AnsichtTab_K where ansicht = 'FX_AnsichtDef_K' and jointyp = 'x'),
(select FX_TabSpEig_K.dsid from FX_TabSpEig_K join FX_TabDef_K on FX_TabSpEig_K.TABDEFID = FX_TabDef_K.DSID where FX_TabDef_K.tabelle = 'FX_AnsichtDef_K' and FX_TabSpEig_K.spalte = 'Sortieren'));

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_AnsichtSpalten_K:DSID';

Insert into FX_ANSICHTSPALTEN_K (INDEXGRID,TABAKEY,SPALTEAKEY,INGRIDANZEIGEN,INGRIDLADEN,FILTER,ANZAHLNACHKOMMASTELLEN,BEARBHINZUFZUGELASSEN,BEARBZUGELASSEN,EINGABENOTWENDIG,ANSICHTSUCHEN,SUCHWERTAUSTABAKEY,SUCHWERTAUSSPALTEAKEY,WHERETABAKEY,WHERESPALTEAKEY,VERDECKENDURCHTABAKEY,VERDECKENDURCHSPALTEAKEY,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,ANSICHTDEFID,ANSICHTTABID,TABSPEIGID) values ('32','FX_AnsichtDef_K','System','-1','-1','-1',null,null,null,null,null,null,null,null,null,null,null,'MW',to_date('04.07.18','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_AnsichtSpalten_K:DSID'),
(select dsid from FX_AnsichtDef_K where ansicht = 'FX_AnsichtDef_K'),
(select dsid from FX_AnsichtTab_K where ansicht = 'FX_AnsichtDef_K' and jointyp = 'x'),
(select FX_TabSpEig_K.dsid from FX_TabSpEig_K join FX_TabDef_K on FX_TabSpEig_K.TABDEFID = FX_TabDef_K.DSID where FX_TabDef_K.tabelle = 'FX_AnsichtDef_K' and FX_TabSpEig_K.spalte = 'System'));

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_AnsichtSpalten_K:DSID';

Insert into FX_ANSICHTSPALTEN_K (INDEXGRID,TABAKEY,SPALTEAKEY,INGRIDANZEIGEN,INGRIDLADEN,FILTER,ANZAHLNACHKOMMASTELLEN,BEARBHINZUFZUGELASSEN,BEARBZUGELASSEN,EINGABENOTWENDIG,ANSICHTSUCHEN,SUCHWERTAUSTABAKEY,SUCHWERTAUSSPALTEAKEY,WHERETABAKEY,WHERESPALTEAKEY,VERDECKENDURCHTABAKEY,VERDECKENDURCHSPALTEAKEY,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,ANSICHTDEFID,ANSICHTTABID,TABSPEIGID) values ('33','FX_AnsichtDef_K','urlPath','-1','-1','-1',null,'-1','-1',null,null,null,null,null,null,null,null,'MW#migration#',to_date('04.07.18','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_AnsichtSpalten_K:DSID'),
(select dsid from FX_AnsichtDef_K where ansicht = 'FX_AnsichtDef_K'),
(select dsid from FX_AnsichtTab_K where ansicht = 'FX_AnsichtDef_K' and jointyp = 'x'),
(select FX_TabSpEig_K.dsid from FX_TabSpEig_K join FX_TabDef_K on FX_TabSpEig_K.TABDEFID = FX_TabDef_K.DSID where FX_TabDef_K.tabelle = 'FX_AnsichtDef_K' and FX_TabSpEig_K.spalte = 'urlPath'));

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_AnsichtSpalten_K:DSID';

-- insert FX_AnsichtSpalten datasets for FX_AnsichtSpalten_K
Insert into FX_ANSICHTSPALTEN_K (INDEXGRID,TABAKEY,SPALTEAKEY,INGRIDANZEIGEN,INGRIDLADEN,FILTER,ANZAHLNACHKOMMASTELLEN,BEARBHINZUFZUGELASSEN,BEARBZUGELASSEN,EINGABENOTWENDIG,ANSICHTSUCHEN,SUCHWERTAUSTABAKEY,SUCHWERTAUSSPALTEAKEY,WHERETABAKEY,WHERESPALTEAKEY,VERDECKENDURCHTABAKEY,VERDECKENDURCHSPALTEAKEY,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,ANSICHTDEFID,ANSICHTTABID,TABSPEIGID) values ('41','FX_AnsichtSpalten_K','AngelegtVon','-1','-1','-1',null,null,null,null,null,null,null,null,null,null,null,'MW',to_date('04.07.18','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_AnsichtSpalten_K:DSID'),
(select dsid from FX_AnsichtDef_K where ansicht = 'FX_AnsichtSpalten_K'),
(select dsid from FX_AnsichtTab_K where ansicht = 'FX_AnsichtSpalten_K' and jointyp = 'x'),
(select FX_TabSpEig_K.dsid from FX_TabSpEig_K join FX_TabDef_K on FX_TabSpEig_K.TABDEFID = FX_TabDef_K.DSID where FX_TabDef_K.tabelle = 'FX_AnsichtSpalten_K' and FX_TabSpEig_K.spalte = 'AngelegtVon'));

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_AnsichtSpalten_K:DSID';

Insert into FX_ANSICHTSPALTEN_K (INDEXGRID,TABAKEY,SPALTEAKEY,INGRIDANZEIGEN,INGRIDLADEN,FILTER,ANZAHLNACHKOMMASTELLEN,BEARBHINZUFZUGELASSEN,BEARBZUGELASSEN,EINGABENOTWENDIG,ANSICHTSUCHEN,SUCHWERTAUSTABAKEY,SUCHWERTAUSSPALTEAKEY,WHERETABAKEY,WHERESPALTEAKEY,VERDECKENDURCHTABAKEY,VERDECKENDURCHSPALTEAKEY,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,ANSICHTDEFID,ANSICHTTABID,TABSPEIGID) values ('21','FX_AnsichtSpalten_K','AnsichtSuchen','-1','-1','-1',null,null,null,null,null,null,null,null,null,null,null,'MW',to_date('04.07.18','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_AnsichtSpalten_K:DSID'),
(select dsid from FX_AnsichtDef_K where ansicht = 'FX_AnsichtSpalten_K'),
(select dsid from FX_AnsichtTab_K where ansicht = 'FX_AnsichtSpalten_K' and jointyp = 'x'),
(select FX_TabSpEig_K.dsid from FX_TabSpEig_K join FX_TabDef_K on FX_TabSpEig_K.TABDEFID = FX_TabDef_K.DSID where FX_TabDef_K.tabelle = 'FX_AnsichtSpalten_K' and FX_TabSpEig_K.spalte = 'AnsichtSuchen'));

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_AnsichtSpalten_K:DSID';

Insert into FX_ANSICHTSPALTEN_K (INDEXGRID,TABAKEY,SPALTEAKEY,INGRIDANZEIGEN,INGRIDLADEN,FILTER,ANZAHLNACHKOMMASTELLEN,BEARBHINZUFZUGELASSEN,BEARBZUGELASSEN,EINGABENOTWENDIG,ANSICHTSUCHEN,SUCHWERTAUSTABAKEY,SUCHWERTAUSSPALTEAKEY,WHERETABAKEY,WHERESPALTEAKEY,VERDECKENDURCHTABAKEY,VERDECKENDURCHSPALTEAKEY,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,ANSICHTDEFID,ANSICHTTABID,TABSPEIGID) values ('12','FX_AnsichtSpalten_K','AnzahlNachkommastellen','-1','-1','-1',null,null,null,null,null,null,null,null,null,null,null,'MW',to_date('04.07.18','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_AnsichtSpalten_K:DSID'),
(select dsid from FX_AnsichtDef_K where ansicht = 'FX_AnsichtSpalten_K'),
(select dsid from FX_AnsichtTab_K where ansicht = 'FX_AnsichtSpalten_K' and jointyp = 'x'),
(select FX_TabSpEig_K.dsid from FX_TabSpEig_K join FX_TabDef_K on FX_TabSpEig_K.TABDEFID = FX_TabDef_K.DSID where FX_TabDef_K.tabelle = 'FX_AnsichtSpalten_K' and FX_TabSpEig_K.spalte = 'AnzahlNachkommastellen'));

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_AnsichtSpalten_K:DSID';

Insert into FX_ANSICHTSPALTEN_K (INDEXGRID,TABAKEY,SPALTEAKEY,INGRIDANZEIGEN,INGRIDLADEN,FILTER,ANZAHLNACHKOMMASTELLEN,BEARBHINZUFZUGELASSEN,BEARBZUGELASSEN,EINGABENOTWENDIG,ANSICHTSUCHEN,SUCHWERTAUSTABAKEY,SUCHWERTAUSSPALTEAKEY,WHERETABAKEY,WHERESPALTEAKEY,VERDECKENDURCHTABAKEY,VERDECKENDURCHSPALTEAKEY,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,ANSICHTDEFID,ANSICHTTABID,TABSPEIGID) values ('15','FX_AnsichtSpalten_K','BearbHinZufZugelassen','-1','-1','-1',null,null,null,null,null,null,null,null,null,null,null,'MW',to_date('04.07.18','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_AnsichtSpalten_K:DSID'),
(select dsid from FX_AnsichtDef_K where ansicht = 'FX_AnsichtSpalten_K'),
(select dsid from FX_AnsichtTab_K where ansicht = 'FX_AnsichtSpalten_K' and jointyp = 'x'),
(select FX_TabSpEig_K.dsid from FX_TabSpEig_K join FX_TabDef_K on FX_TabSpEig_K.TABDEFID = FX_TabDef_K.DSID where FX_TabDef_K.tabelle = 'FX_AnsichtSpalten_K' and FX_TabSpEig_K.spalte = 'BearbHinZufZugelassen'));

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_AnsichtSpalten_K:DSID';

Insert into FX_ANSICHTSPALTEN_K (INDEXGRID,TABAKEY,SPALTEAKEY,INGRIDANZEIGEN,INGRIDLADEN,FILTER,ANZAHLNACHKOMMASTELLEN,BEARBHINZUFZUGELASSEN,BEARBZUGELASSEN,EINGABENOTWENDIG,ANSICHTSUCHEN,SUCHWERTAUSTABAKEY,SUCHWERTAUSSPALTEAKEY,WHERETABAKEY,WHERESPALTEAKEY,VERDECKENDURCHTABAKEY,VERDECKENDURCHSPALTEAKEY,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,ANSICHTDEFID,ANSICHTTABID,TABSPEIGID) values ('16','FX_AnsichtSpalten_K','BearbZugelassen','-1','-1','-1',null,null,null,null,null,null,null,null,null,null,null,'MW',to_date('04.07.18','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_AnsichtSpalten_K:DSID'),
(select dsid from FX_AnsichtDef_K where ansicht = 'FX_AnsichtSpalten_K'),
(select dsid from FX_AnsichtTab_K where ansicht = 'FX_AnsichtSpalten_K' and jointyp = 'x'),
(select FX_TabSpEig_K.dsid from FX_TabSpEig_K join FX_TabDef_K on FX_TabSpEig_K.TABDEFID = FX_TabDef_K.DSID where FX_TabDef_K.tabelle = 'FX_AnsichtSpalten_K' and FX_TabSpEig_K.spalte = 'BearbZugelassen'));

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_AnsichtSpalten_K:DSID';

Insert into FX_ANSICHTSPALTEN_K (INDEXGRID,TABAKEY,SPALTEAKEY,INGRIDANZEIGEN,INGRIDLADEN,FILTER,ANZAHLNACHKOMMASTELLEN,BEARBHINZUFZUGELASSEN,BEARBZUGELASSEN,EINGABENOTWENDIG,ANSICHTSUCHEN,SUCHWERTAUSTABAKEY,SUCHWERTAUSSPALTEAKEY,WHERETABAKEY,WHERESPALTEAKEY,VERDECKENDURCHTABAKEY,VERDECKENDURCHSPALTEAKEY,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,ANSICHTDEFID,ANSICHTTABID,TABSPEIGID) values ('40','FX_AnsichtSpalten_K','DSID','-1','-1','-1',null,null,null,null,null,null,null,null,null,null,null,'MW',to_date('04.07.18','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_AnsichtSpalten_K:DSID'),
(select dsid from FX_AnsichtDef_K where ansicht = 'FX_AnsichtSpalten_K'),
(select dsid from FX_AnsichtTab_K where ansicht = 'FX_AnsichtSpalten_K' and jointyp = 'x'),
(select FX_TabSpEig_K.dsid from FX_TabSpEig_K join FX_TabDef_K on FX_TabSpEig_K.TABDEFID = FX_TabDef_K.DSID where FX_TabDef_K.tabelle = 'FX_AnsichtSpalten_K' and FX_TabSpEig_K.spalte = 'DSID'));

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_AnsichtSpalten_K:DSID';

Insert into FX_ANSICHTSPALTEN_K (INDEXGRID,TABAKEY,SPALTEAKEY,INGRIDANZEIGEN,INGRIDLADEN,FILTER,ANZAHLNACHKOMMASTELLEN,BEARBHINZUFZUGELASSEN,BEARBZUGELASSEN,EINGABENOTWENDIG,ANSICHTSUCHEN,SUCHWERTAUSTABAKEY,SUCHWERTAUSSPALTEAKEY,WHERETABAKEY,WHERESPALTEAKEY,VERDECKENDURCHTABAKEY,VERDECKENDURCHSPALTEAKEY,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,ANSICHTDEFID,ANSICHTTABID,TABSPEIGID) values ('17','FX_AnsichtSpalten_K','EingabeNotwendig','-1','-1','-1',null,null,null,null,null,null,null,null,null,null,null,'MW',to_date('04.07.18','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_AnsichtSpalten_K:DSID'),
(select dsid from FX_AnsichtDef_K where ansicht = 'FX_AnsichtSpalten_K'),
(select dsid from FX_AnsichtTab_K where ansicht = 'FX_AnsichtSpalten_K' and jointyp = 'x'),
(select FX_TabSpEig_K.dsid from FX_TabSpEig_K join FX_TabDef_K on FX_TabSpEig_K.TABDEFID = FX_TabDef_K.DSID where FX_TabDef_K.tabelle = 'FX_AnsichtSpalten_K' and FX_TabSpEig_K.spalte = 'EingabeNotwendig'));

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_AnsichtSpalten_K:DSID';

Insert into FX_ANSICHTSPALTEN_K (INDEXGRID,TABAKEY,SPALTEAKEY,INGRIDANZEIGEN,INGRIDLADEN,FILTER,ANZAHLNACHKOMMASTELLEN,BEARBHINZUFZUGELASSEN,BEARBZUGELASSEN,EINGABENOTWENDIG,ANSICHTSUCHEN,SUCHWERTAUSTABAKEY,SUCHWERTAUSSPALTEAKEY,WHERETABAKEY,WHERESPALTEAKEY,VERDECKENDURCHTABAKEY,VERDECKENDURCHSPALTEAKEY,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,ANSICHTDEFID,ANSICHTTABID,TABSPEIGID) values ('9','FX_AnsichtSpalten_K','Filter','-1','-1','-1',null,null,null,null,null,null,null,null,null,null,null,'MW',to_date('04.07.18','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_AnsichtSpalten_K:DSID'),
(select dsid from FX_AnsichtDef_K where ansicht = 'FX_AnsichtSpalten_K'),
(select dsid from FX_AnsichtTab_K where ansicht = 'FX_AnsichtSpalten_K' and jointyp = 'x'),
(select FX_TabSpEig_K.dsid from FX_TabSpEig_K join FX_TabDef_K on FX_TabSpEig_K.TABDEFID = FX_TabDef_K.DSID where FX_TabDef_K.tabelle = 'FX_AnsichtSpalten_K' and FX_TabSpEig_K.spalte = 'Filter'));

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_AnsichtSpalten_K:DSID';

Insert into FX_ANSICHTSPALTEN_K (INDEXGRID,TABAKEY,SPALTEAKEY,INGRIDANZEIGEN,INGRIDLADEN,FILTER,ANZAHLNACHKOMMASTELLEN,BEARBHINZUFZUGELASSEN,BEARBZUGELASSEN,EINGABENOTWENDIG,ANSICHTSUCHEN,SUCHWERTAUSTABAKEY,SUCHWERTAUSSPALTEAKEY,WHERETABAKEY,WHERESPALTEAKEY,VERDECKENDURCHTABAKEY,VERDECKENDURCHSPALTEAKEY,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,ANSICHTDEFID,ANSICHTTABID,TABSPEIGID) values ('7','FX_AnsichtSpalten_K','InGridAnzeigen','-1','-1','-1',null,null,null,null,null,null,null,null,null,null,null,'MW',to_date('04.07.18','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_AnsichtSpalten_K:DSID'),
(select dsid from FX_AnsichtDef_K where ansicht = 'FX_AnsichtSpalten_K'),
(select dsid from FX_AnsichtTab_K where ansicht = 'FX_AnsichtSpalten_K' and jointyp = 'x'),
(select FX_TabSpEig_K.dsid from FX_TabSpEig_K join FX_TabDef_K on FX_TabSpEig_K.TABDEFID = FX_TabDef_K.DSID where FX_TabDef_K.tabelle = 'FX_AnsichtSpalten_K' and FX_TabSpEig_K.spalte = 'InGridAnzeigen'));

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_AnsichtSpalten_K:DSID';

Insert into FX_ANSICHTSPALTEN_K (INDEXGRID,TABAKEY,SPALTEAKEY,INGRIDANZEIGEN,INGRIDLADEN,FILTER,ANZAHLNACHKOMMASTELLEN,BEARBHINZUFZUGELASSEN,BEARBZUGELASSEN,EINGABENOTWENDIG,ANSICHTSUCHEN,SUCHWERTAUSTABAKEY,SUCHWERTAUSSPALTEAKEY,WHERETABAKEY,WHERESPALTEAKEY,VERDECKENDURCHTABAKEY,VERDECKENDURCHSPALTEAKEY,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,ANSICHTDEFID,ANSICHTTABID,TABSPEIGID) values ('8','FX_AnsichtSpalten_K','InGridLaden','-1','-1','-1',null,null,null,null,null,null,null,null,null,null,null,'MW',to_date('04.07.18','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_AnsichtSpalten_K:DSID'),
(select dsid from FX_AnsichtDef_K where ansicht = 'FX_AnsichtSpalten_K'),
(select dsid from FX_AnsichtTab_K where ansicht = 'FX_AnsichtSpalten_K' and jointyp = 'x'),
(select FX_TabSpEig_K.dsid from FX_TabSpEig_K join FX_TabDef_K on FX_TabSpEig_K.TABDEFID = FX_TabDef_K.DSID where FX_TabDef_K.tabelle = 'FX_AnsichtSpalten_K' and FX_TabSpEig_K.spalte = 'InGridLaden'));

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_AnsichtSpalten_K:DSID';

Insert into FX_ANSICHTSPALTEN_K (INDEXGRID,TABAKEY,SPALTEAKEY,INGRIDANZEIGEN,INGRIDLADEN,FILTER,ANZAHLNACHKOMMASTELLEN,BEARBHINZUFZUGELASSEN,BEARBZUGELASSEN,EINGABENOTWENDIG,ANSICHTSUCHEN,SUCHWERTAUSTABAKEY,SUCHWERTAUSSPALTEAKEY,WHERETABAKEY,WHERESPALTEAKEY,VERDECKENDURCHTABAKEY,VERDECKENDURCHSPALTEAKEY,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,ANSICHTDEFID,ANSICHTTABID,TABSPEIGID) values ('2','FX_AnsichtSpalten_K','IndexGrid','-1','-1','-1',null,null,null,null,null,null,null,null,null,null,null,'MW',to_date('04.07.18','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_AnsichtSpalten_K:DSID'),
(select dsid from FX_AnsichtDef_K where ansicht = 'FX_AnsichtSpalten_K'),
(select dsid from FX_AnsichtTab_K where ansicht = 'FX_AnsichtSpalten_K' and jointyp = 'x'),
(select FX_TabSpEig_K.dsid from FX_TabSpEig_K join FX_TabDef_K on FX_TabSpEig_K.TABDEFID = FX_TabDef_K.DSID where FX_TabDef_K.tabelle = 'FX_AnsichtSpalten_K' and FX_TabSpEig_K.spalte = 'IndexGrid'));

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_AnsichtSpalten_K:DSID';

Insert into FX_ANSICHTSPALTEN_K (INDEXGRID,TABAKEY,SPALTEAKEY,INGRIDANZEIGEN,INGRIDLADEN,FILTER,ANZAHLNACHKOMMASTELLEN,BEARBHINZUFZUGELASSEN,BEARBZUGELASSEN,EINGABENOTWENDIG,ANSICHTSUCHEN,SUCHWERTAUSTABAKEY,SUCHWERTAUSSPALTEAKEY,WHERETABAKEY,WHERESPALTEAKEY,VERDECKENDURCHTABAKEY,VERDECKENDURCHSPALTEAKEY,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,ANSICHTDEFID,ANSICHTTABID,TABSPEIGID) values ('4','FX_AnsichtSpalten_K','SpalteAKey','-1','-1','-1',null,null,null,null,null,null,null,null,null,null,null,'MW',to_date('04.07.18','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_AnsichtSpalten_K:DSID'),
(select dsid from FX_AnsichtDef_K where ansicht = 'FX_AnsichtSpalten_K'),
(select dsid from FX_AnsichtTab_K where ansicht = 'FX_AnsichtSpalten_K' and jointyp = 'x'),
(select FX_TabSpEig_K.dsid from FX_TabSpEig_K join FX_TabDef_K on FX_TabSpEig_K.TABDEFID = FX_TabDef_K.DSID where FX_TabDef_K.tabelle = 'FX_AnsichtSpalten_K' and FX_TabSpEig_K.spalte = 'SpalteAKey'));

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_AnsichtSpalten_K:DSID';

Insert into FX_ANSICHTSPALTEN_K (INDEXGRID,TABAKEY,SPALTEAKEY,INGRIDANZEIGEN,INGRIDLADEN,FILTER,ANZAHLNACHKOMMASTELLEN,BEARBHINZUFZUGELASSEN,BEARBZUGELASSEN,EINGABENOTWENDIG,ANSICHTSUCHEN,SUCHWERTAUSTABAKEY,SUCHWERTAUSSPALTEAKEY,WHERETABAKEY,WHERESPALTEAKEY,VERDECKENDURCHTABAKEY,VERDECKENDURCHSPALTEAKEY,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,ANSICHTDEFID,ANSICHTTABID,TABSPEIGID) values ('24','FX_AnsichtSpalten_K','SuchWertAusSpalteAKey','-1','-1','-1',null,null,null,null,null,null,null,null,null,null,null,'MW',to_date('04.07.18','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_AnsichtSpalten_K:DSID'),
(select dsid from FX_AnsichtDef_K where ansicht = 'FX_AnsichtSpalten_K'),
(select dsid from FX_AnsichtTab_K where ansicht = 'FX_AnsichtSpalten_K' and jointyp = 'x'),
(select FX_TabSpEig_K.dsid from FX_TabSpEig_K join FX_TabDef_K on FX_TabSpEig_K.TABDEFID = FX_TabDef_K.DSID where FX_TabDef_K.tabelle = 'FX_AnsichtSpalten_K' and FX_TabSpEig_K.spalte = 'SuchWertAusSpalteAKey'));

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_AnsichtSpalten_K:DSID';

Insert into FX_ANSICHTSPALTEN_K (INDEXGRID,TABAKEY,SPALTEAKEY,INGRIDANZEIGEN,INGRIDLADEN,FILTER,ANZAHLNACHKOMMASTELLEN,BEARBHINZUFZUGELASSEN,BEARBZUGELASSEN,EINGABENOTWENDIG,ANSICHTSUCHEN,SUCHWERTAUSTABAKEY,SUCHWERTAUSSPALTEAKEY,WHERETABAKEY,WHERESPALTEAKEY,VERDECKENDURCHTABAKEY,VERDECKENDURCHSPALTEAKEY,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,ANSICHTDEFID,ANSICHTTABID,TABSPEIGID) values ('23','FX_AnsichtSpalten_K','SuchWertAusTabAKey','-1','-1','-1',null,null,null,null,null,null,null,null,null,null,null,'MW',to_date('04.07.18','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_AnsichtSpalten_K:DSID'),
(select dsid from FX_AnsichtDef_K where ansicht = 'FX_AnsichtSpalten_K'),
(select dsid from FX_AnsichtTab_K where ansicht = 'FX_AnsichtSpalten_K' and jointyp = 'x'),
(select FX_TabSpEig_K.dsid from FX_TabSpEig_K join FX_TabDef_K on FX_TabSpEig_K.TABDEFID = FX_TabDef_K.DSID where FX_TabDef_K.tabelle = 'FX_AnsichtSpalten_K' and FX_TabSpEig_K.spalte = 'SuchWertAusTabAKey'));

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_AnsichtSpalten_K:DSID';

Insert into FX_ANSICHTSPALTEN_K (INDEXGRID,TABAKEY,SPALTEAKEY,INGRIDANZEIGEN,INGRIDLADEN,FILTER,ANZAHLNACHKOMMASTELLEN,BEARBHINZUFZUGELASSEN,BEARBZUGELASSEN,EINGABENOTWENDIG,ANSICHTSUCHEN,SUCHWERTAUSTABAKEY,SUCHWERTAUSSPALTEAKEY,WHERETABAKEY,WHERESPALTEAKEY,VERDECKENDURCHTABAKEY,VERDECKENDURCHSPALTEAKEY,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,ANSICHTDEFID,ANSICHTTABID,TABSPEIGID) values ('46','FX_AnsichtSpalten_K','System','-1','-1','-1',null,null,null,null,null,null,null,null,null,null,null,'MW',to_date('04.07.18','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_AnsichtSpalten_K:DSID'),
(select dsid from FX_AnsichtDef_K where ansicht = 'FX_AnsichtSpalten_K'),
(select dsid from FX_AnsichtTab_K where ansicht = 'FX_AnsichtSpalten_K' and jointyp = 'x'),
(select FX_TabSpEig_K.dsid from FX_TabSpEig_K join FX_TabDef_K on FX_TabSpEig_K.TABDEFID = FX_TabDef_K.DSID where FX_TabDef_K.tabelle = 'FX_AnsichtSpalten_K' and FX_TabSpEig_K.spalte = 'System'));

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_AnsichtSpalten_K:DSID';

Insert into FX_ANSICHTSPALTEN_K (INDEXGRID,TABAKEY,SPALTEAKEY,INGRIDANZEIGEN,INGRIDLADEN,FILTER,ANZAHLNACHKOMMASTELLEN,BEARBHINZUFZUGELASSEN,BEARBZUGELASSEN,EINGABENOTWENDIG,ANSICHTSUCHEN,SUCHWERTAUSTABAKEY,SUCHWERTAUSSPALTEAKEY,WHERETABAKEY,WHERESPALTEAKEY,VERDECKENDURCHTABAKEY,VERDECKENDURCHSPALTEAKEY,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,ANSICHTDEFID,ANSICHTTABID,TABSPEIGID) values ('3','FX_AnsichtSpalten_K','TabAKey','-1','-1','-1',null,null,null,null,null,null,null,null,null,null,null,'MW',to_date('04.07.18','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_AnsichtSpalten_K:DSID'),
(select dsid from FX_AnsichtDef_K where ansicht = 'FX_AnsichtSpalten_K'),
(select dsid from FX_AnsichtTab_K where ansicht = 'FX_AnsichtSpalten_K' and jointyp = 'x'),
(select FX_TabSpEig_K.dsid from FX_TabSpEig_K join FX_TabDef_K on FX_TabSpEig_K.TABDEFID = FX_TabDef_K.DSID where FX_TabDef_K.tabelle = 'FX_AnsichtSpalten_K' and FX_TabSpEig_K.spalte = 'TabAKey'));

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_AnsichtSpalten_K:DSID';

Insert into FX_ANSICHTSPALTEN_K (INDEXGRID,TABAKEY,SPALTEAKEY,INGRIDANZEIGEN,INGRIDLADEN,FILTER,ANZAHLNACHKOMMASTELLEN,BEARBHINZUFZUGELASSEN,BEARBZUGELASSEN,EINGABENOTWENDIG,ANSICHTSUCHEN,SUCHWERTAUSTABAKEY,SUCHWERTAUSSPALTEAKEY,WHERETABAKEY,WHERESPALTEAKEY,VERDECKENDURCHTABAKEY,VERDECKENDURCHSPALTEAKEY,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,ANSICHTDEFID,ANSICHTTABID,TABSPEIGID) values ('31','FX_AnsichtSpalten_K','VerdeckenDurchSpalteAKey','-1','-1','-1',null,null,null,null,null,null,null,null,null,null,null,'MW',to_date('04.07.18','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_AnsichtSpalten_K:DSID'),
(select dsid from FX_AnsichtDef_K where ansicht = 'FX_AnsichtSpalten_K'),
(select dsid from FX_AnsichtTab_K where ansicht = 'FX_AnsichtSpalten_K' and jointyp = 'x'),
(select FX_TabSpEig_K.dsid from FX_TabSpEig_K join FX_TabDef_K on FX_TabSpEig_K.TABDEFID = FX_TabDef_K.DSID where FX_TabDef_K.tabelle = 'FX_AnsichtSpalten_K' and FX_TabSpEig_K.spalte = 'VerdeckenDurchSpalteAKey'));

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_AnsichtSpalten_K:DSID';

Insert into FX_ANSICHTSPALTEN_K (INDEXGRID,TABAKEY,SPALTEAKEY,INGRIDANZEIGEN,INGRIDLADEN,FILTER,ANZAHLNACHKOMMASTELLEN,BEARBHINZUFZUGELASSEN,BEARBZUGELASSEN,EINGABENOTWENDIG,ANSICHTSUCHEN,SUCHWERTAUSTABAKEY,SUCHWERTAUSSPALTEAKEY,WHERETABAKEY,WHERESPALTEAKEY,VERDECKENDURCHTABAKEY,VERDECKENDURCHSPALTEAKEY,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,ANSICHTDEFID,ANSICHTTABID,TABSPEIGID) values ('30','FX_AnsichtSpalten_K','VerdeckenDurchTabAKey','-1','-1','-1',null,null,null,null,null,null,null,null,null,null,null,'MW',to_date('04.07.18','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_AnsichtSpalten_K:DSID'),
(select dsid from FX_AnsichtDef_K where ansicht = 'FX_AnsichtSpalten_K'),
(select dsid from FX_AnsichtTab_K where ansicht = 'FX_AnsichtSpalten_K' and jointyp = 'x'),
(select FX_TabSpEig_K.dsid from FX_TabSpEig_K join FX_TabDef_K on FX_TabSpEig_K.TABDEFID = FX_TabDef_K.DSID where FX_TabDef_K.tabelle = 'FX_AnsichtSpalten_K' and FX_TabSpEig_K.spalte = 'VerdeckenDurchTabAKey'));

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_AnsichtSpalten_K:DSID';

Insert into FX_ANSICHTSPALTEN_K (INDEXGRID,TABAKEY,SPALTEAKEY,INGRIDANZEIGEN,INGRIDLADEN,FILTER,ANZAHLNACHKOMMASTELLEN,BEARBHINZUFZUGELASSEN,BEARBZUGELASSEN,EINGABENOTWENDIG,ANSICHTSUCHEN,SUCHWERTAUSTABAKEY,SUCHWERTAUSSPALTEAKEY,WHERETABAKEY,WHERESPALTEAKEY,VERDECKENDURCHTABAKEY,VERDECKENDURCHSPALTEAKEY,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,ANSICHTDEFID,ANSICHTTABID,TABSPEIGID) values ('26','FX_AnsichtSpalten_K','WhereSpalteAKey','-1','-1','-1',null,null,null,null,null,null,null,null,null,null,null,'MW',to_date('04.07.18','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_AnsichtSpalten_K:DSID'),
(select dsid from FX_AnsichtDef_K where ansicht = 'FX_AnsichtSpalten_K'),
(select dsid from FX_AnsichtTab_K where ansicht = 'FX_AnsichtSpalten_K' and jointyp = 'x'),
(select FX_TabSpEig_K.dsid from FX_TabSpEig_K join FX_TabDef_K on FX_TabSpEig_K.TABDEFID = FX_TabDef_K.DSID where FX_TabDef_K.tabelle = 'FX_AnsichtSpalten_K' and FX_TabSpEig_K.spalte = 'WhereSpalteAKey'));

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_AnsichtSpalten_K:DSID';

Insert into FX_ANSICHTSPALTEN_K (INDEXGRID,TABAKEY,SPALTEAKEY,INGRIDANZEIGEN,INGRIDLADEN,FILTER,ANZAHLNACHKOMMASTELLEN,BEARBHINZUFZUGELASSEN,BEARBZUGELASSEN,EINGABENOTWENDIG,ANSICHTSUCHEN,SUCHWERTAUSTABAKEY,SUCHWERTAUSSPALTEAKEY,WHERETABAKEY,WHERESPALTEAKEY,VERDECKENDURCHTABAKEY,VERDECKENDURCHSPALTEAKEY,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,ANSICHTDEFID,ANSICHTTABID,TABSPEIGID) values ('42','FX_AnsichtSpalten_K','angelegtAm','-1','-1','-1',null,null,null,null,null,null,null,null,null,null,null,'MW',to_date('04.07.18','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_AnsichtSpalten_K:DSID'),
(select dsid from FX_AnsichtDef_K where ansicht = 'FX_AnsichtSpalten_K'),
(select dsid from FX_AnsichtTab_K where ansicht = 'FX_AnsichtSpalten_K' and jointyp = 'x'),
(select FX_TabSpEig_K.dsid from FX_TabSpEig_K join FX_TabDef_K on FX_TabSpEig_K.TABDEFID = FX_TabDef_K.DSID where FX_TabDef_K.tabelle = 'FX_AnsichtSpalten_K' and FX_TabSpEig_K.spalte = 'angelegtAm'));

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_AnsichtSpalten_K:DSID';

-- insert FX_AnsichtSpalten datasets for FX_AnsichtORderBy_K
Insert into FX_ANSICHTSPALTEN_K (INDEXGRID,TABAKEY,SPALTEAKEY,INGRIDANZEIGEN,INGRIDLADEN,FILTER,ANZAHLNACHKOMMASTELLEN,BEARBHINZUFZUGELASSEN,BEARBZUGELASSEN,EINGABENOTWENDIG,ANSICHTSUCHEN,SUCHWERTAUSTABAKEY,SUCHWERTAUSSPALTEAKEY,WHERETABAKEY,WHERESPALTEAKEY,VERDECKENDURCHTABAKEY,VERDECKENDURCHSPALTEAKEY,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,ANSICHTDEFID,ANSICHTTABID,TABSPEIGID) values ('1','FX_AnsichtOrderBy_K','Ansicht','-1','-1','-1',null,null,null,null,null,null,null,null,null,null,null,'MW',to_date('04.07.18','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_AnsichtSpalten_K:DSID'),
(select dsid from FX_AnsichtDef_K where ansicht = 'FX_AnsichtOrderBy_K'),
(select dsid from FX_AnsichtTab_K where ansicht = 'FX_AnsichtOrderBy_K' and jointyp = 'x'),
(select FX_TabSpEig_K.dsid from FX_TabSpEig_K join FX_TabDef_K on FX_TabSpEig_K.TABDEFID = FX_TabDef_K.DSID where FX_TabDef_K.tabelle = 'FX_AnsichtOrderBy_K' and FX_TabSpEig_K.spalte = 'Ansicht'));

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_AnsichtSpalten_K:DSID';

Insert into FX_ANSICHTSPALTEN_K (INDEXGRID,TABAKEY,SPALTEAKEY,INGRIDANZEIGEN,INGRIDLADEN,FILTER,ANZAHLNACHKOMMASTELLEN,BEARBHINZUFZUGELASSEN,BEARBZUGELASSEN,EINGABENOTWENDIG,ANSICHTSUCHEN,SUCHWERTAUSTABAKEY,SUCHWERTAUSSPALTEAKEY,WHERETABAKEY,WHERESPALTEAKEY,VERDECKENDURCHTABAKEY,VERDECKENDURCHSPALTEAKEY,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,ANSICHTDEFID,ANSICHTTABID,TABSPEIGID) values ('5','FX_AnsichtOrderBy_K','Aufsteigend','-1','-1','-1',null,null,null,null,null,null,null,null,null,null,null,'MW',to_date('04.07.18','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_AnsichtSpalten_K:DSID'),
(select dsid from FX_AnsichtDef_K where ansicht = 'FX_AnsichtOrderBy_K'),
(select dsid from FX_AnsichtTab_K where ansicht = 'FX_AnsichtOrderBy_K' and jointyp = 'x'),
(select FX_TabSpEig_K.dsid from FX_TabSpEig_K join FX_TabDef_K on FX_TabSpEig_K.TABDEFID = FX_TabDef_K.DSID where FX_TabDef_K.tabelle = 'FX_AnsichtOrderBy_K' and FX_TabSpEig_K.spalte = 'Aufsteigend'));

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_AnsichtSpalten_K:DSID';

Insert into FX_ANSICHTSPALTEN_K (INDEXGRID,TABAKEY,SPALTEAKEY,INGRIDANZEIGEN,INGRIDLADEN,FILTER,ANZAHLNACHKOMMASTELLEN,BEARBHINZUFZUGELASSEN,BEARBZUGELASSEN,EINGABENOTWENDIG,ANSICHTSUCHEN,SUCHWERTAUSTABAKEY,SUCHWERTAUSSPALTEAKEY,WHERETABAKEY,WHERESPALTEAKEY,VERDECKENDURCHTABAKEY,VERDECKENDURCHSPALTEAKEY,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,ANSICHTDEFID,ANSICHTTABID,TABSPEIGID) values ('6','FX_AnsichtOrderBy_K','DSID','-1','-1','-1',null,null,null,null,null,null,null,null,null,null,null,'MW',to_date('04.07.18','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_AnsichtSpalten_K:DSID'),
(select dsid from FX_AnsichtDef_K where ansicht = 'FX_AnsichtOrderBy_K'),
(select dsid from FX_AnsichtTab_K where ansicht = 'FX_AnsichtOrderBy_K' and jointyp = 'x'),
(select FX_TabSpEig_K.dsid from FX_TabSpEig_K join FX_TabDef_K on FX_TabSpEig_K.TABDEFID = FX_TabDef_K.DSID where FX_TabDef_K.tabelle = 'FX_AnsichtOrderBy_K' and FX_TabSpEig_K.spalte = 'DSID'));

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_AnsichtSpalten_K:DSID';

Insert into FX_ANSICHTSPALTEN_K (INDEXGRID,TABAKEY,SPALTEAKEY,INGRIDANZEIGEN,INGRIDLADEN,FILTER,ANZAHLNACHKOMMASTELLEN,BEARBHINZUFZUGELASSEN,BEARBZUGELASSEN,EINGABENOTWENDIG,ANSICHTSUCHEN,SUCHWERTAUSTABAKEY,SUCHWERTAUSSPALTEAKEY,WHERETABAKEY,WHERESPALTEAKEY,VERDECKENDURCHTABAKEY,VERDECKENDURCHSPALTEAKEY,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,ANSICHTDEFID,ANSICHTTABID,TABSPEIGID) values ('2','FX_AnsichtOrderBy_K','Reihenfolge','-1','-1','-1',null,null,null,null,null,null,null,null,null,null,null,'MW',to_date('04.07.18','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_AnsichtSpalten_K:DSID'),
(select dsid from FX_AnsichtDef_K where ansicht = 'FX_AnsichtOrderBy_K'),
(select dsid from FX_AnsichtTab_K where ansicht = 'FX_AnsichtOrderBy_K' and jointyp = 'x'),
(select FX_TabSpEig_K.dsid from FX_TabSpEig_K join FX_TabDef_K on FX_TabSpEig_K.TABDEFID = FX_TabDef_K.DSID where FX_TabDef_K.tabelle = 'FX_AnsichtOrderBy_K' and FX_TabSpEig_K.spalte = 'Reihenfolge'));

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_AnsichtSpalten_K:DSID';

Insert into FX_ANSICHTSPALTEN_K (INDEXGRID,TABAKEY,SPALTEAKEY,INGRIDANZEIGEN,INGRIDLADEN,FILTER,ANZAHLNACHKOMMASTELLEN,BEARBHINZUFZUGELASSEN,BEARBZUGELASSEN,EINGABENOTWENDIG,ANSICHTSUCHEN,SUCHWERTAUSTABAKEY,SUCHWERTAUSSPALTEAKEY,WHERETABAKEY,WHERESPALTEAKEY,VERDECKENDURCHTABAKEY,VERDECKENDURCHSPALTEAKEY,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,ANSICHTDEFID,ANSICHTTABID,TABSPEIGID) values ('4','FX_AnsichtOrderBy_K','SpalteAKey','-1','-1','-1',null,null,null,null,null,null,null,null,null,null,null,'MW',to_date('04.07.18','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_AnsichtSpalten_K:DSID'),
(select dsid from FX_AnsichtDef_K where ansicht = 'FX_AnsichtOrderBy_K'),
(select dsid from FX_AnsichtTab_K where ansicht = 'FX_AnsichtOrderBy_K' and jointyp = 'x'),
(select FX_TabSpEig_K.dsid from FX_TabSpEig_K join FX_TabDef_K on FX_TabSpEig_K.TABDEFID = FX_TabDef_K.DSID where FX_TabDef_K.tabelle = 'FX_AnsichtOrderBy_K' and FX_TabSpEig_K.spalte = 'SpalteAKey'));

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_AnsichtSpalten_K:DSID';

Insert into FX_ANSICHTSPALTEN_K (INDEXGRID,TABAKEY,SPALTEAKEY,INGRIDANZEIGEN,INGRIDLADEN,FILTER,ANZAHLNACHKOMMASTELLEN,BEARBHINZUFZUGELASSEN,BEARBZUGELASSEN,EINGABENOTWENDIG,ANSICHTSUCHEN,SUCHWERTAUSTABAKEY,SUCHWERTAUSSPALTEAKEY,WHERETABAKEY,WHERESPALTEAKEY,VERDECKENDURCHTABAKEY,VERDECKENDURCHSPALTEAKEY,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,ANSICHTDEFID,ANSICHTTABID,TABSPEIGID) values ('10','FX_AnsichtOrderBy_K','System','-1','-1','-1',null,null,null,null,null,null,null,null,null,null,null,'MW',to_date('04.07.18','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_AnsichtSpalten_K:DSID'),
(select dsid from FX_AnsichtDef_K where ansicht = 'FX_AnsichtOrderBy_K'),
(select dsid from FX_AnsichtTab_K where ansicht = 'FX_AnsichtOrderBy_K' and jointyp = 'x'),
(select FX_TabSpEig_K.dsid from FX_TabSpEig_K join FX_TabDef_K on FX_TabSpEig_K.TABDEFID = FX_TabDef_K.DSID where FX_TabDef_K.tabelle = 'FX_AnsichtOrderBy_K' and FX_TabSpEig_K.spalte = 'System'));

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_AnsichtSpalten_K:DSID';

Insert into FX_ANSICHTSPALTEN_K (INDEXGRID,TABAKEY,SPALTEAKEY,INGRIDANZEIGEN,INGRIDLADEN,FILTER,ANZAHLNACHKOMMASTELLEN,BEARBHINZUFZUGELASSEN,BEARBZUGELASSEN,EINGABENOTWENDIG,ANSICHTSUCHEN,SUCHWERTAUSTABAKEY,SUCHWERTAUSSPALTEAKEY,WHERETABAKEY,WHERESPALTEAKEY,VERDECKENDURCHTABAKEY,VERDECKENDURCHSPALTEAKEY,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,ANSICHTDEFID,ANSICHTTABID,TABSPEIGID) values ('3','FX_AnsichtOrderBy_K','TabAKey','-1','-1','-1',null,null,null,null,null,null,null,null,null,null,null,'MW',to_date('04.07.18','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_AnsichtSpalten_K:DSID'),
(select dsid from FX_AnsichtDef_K where ansicht = 'FX_AnsichtOrderBy_K'),
(select dsid from FX_AnsichtTab_K where ansicht = 'FX_AnsichtOrderBy_K' and jointyp = 'x'),
(select FX_TabSpEig_K.dsid from FX_TabSpEig_K join FX_TabDef_K on FX_TabSpEig_K.TABDEFID = FX_TabDef_K.DSID where FX_TabDef_K.tabelle = 'FX_AnsichtOrderBy_K' and FX_TabSpEig_K.spalte = 'TabAKey'));

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_AnsichtSpalten_K:DSID';

Insert into FX_ANSICHTSPALTEN_K (INDEXGRID,TABAKEY,SPALTEAKEY,INGRIDANZEIGEN,INGRIDLADEN,FILTER,ANZAHLNACHKOMMASTELLEN,BEARBHINZUFZUGELASSEN,BEARBZUGELASSEN,EINGABENOTWENDIG,ANSICHTSUCHEN,SUCHWERTAUSTABAKEY,SUCHWERTAUSSPALTEAKEY,WHERETABAKEY,WHERESPALTEAKEY,VERDECKENDURCHTABAKEY,VERDECKENDURCHSPALTEAKEY,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,ANSICHTDEFID,ANSICHTTABID,TABSPEIGID) values ('8','FX_AnsichtOrderBy_K','angelegtAm','-1','-1','-1',null,null,null,null,null,null,null,null,null,null,null,'MW',to_date('04.07.18','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_AnsichtSpalten_K:DSID'),
(select dsid from FX_AnsichtDef_K where ansicht = 'FX_AnsichtOrderBy_K'),
(select dsid from FX_AnsichtTab_K where ansicht = 'FX_AnsichtOrderBy_K' and jointyp = 'x'),
(select FX_TabSpEig_K.dsid from FX_TabSpEig_K join FX_TabDef_K on FX_TabSpEig_K.TABDEFID = FX_TabDef_K.DSID where FX_TabDef_K.tabelle = 'FX_AnsichtOrderBy_K' and FX_TabSpEig_K.spalte = 'angelegtAm'));

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_AnsichtSpalten_K:DSID';

Insert into FX_ANSICHTSPALTEN_K (INDEXGRID,TABAKEY,SPALTEAKEY,INGRIDANZEIGEN,INGRIDLADEN,FILTER,ANZAHLNACHKOMMASTELLEN,BEARBHINZUFZUGELASSEN,BEARBZUGELASSEN,EINGABENOTWENDIG,ANSICHTSUCHEN,SUCHWERTAUSTABAKEY,SUCHWERTAUSSPALTEAKEY,WHERETABAKEY,WHERESPALTEAKEY,VERDECKENDURCHTABAKEY,VERDECKENDURCHSPALTEAKEY,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,ANSICHTDEFID,ANSICHTTABID,TABSPEIGID) values ('7','FX_AnsichtOrderBy_K','angelegtVon','-1','-1','-1',null,null,null,null,null,null,null,null,null,null,null,'MW',to_date('04.07.18','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_AnsichtSpalten_K:DSID'),
(select dsid from FX_AnsichtDef_K where ansicht = 'FX_AnsichtOrderBy_K'),
(select dsid from FX_AnsichtTab_K where ansicht = 'FX_AnsichtOrderBy_K' and jointyp = 'x'),
(select FX_TabSpEig_K.dsid from FX_TabSpEig_K join FX_TabDef_K on FX_TabSpEig_K.TABDEFID = FX_TabDef_K.DSID where FX_TabDef_K.tabelle = 'FX_AnsichtOrderBy_K' and FX_TabSpEig_K.spalte = 'angelegtVon'));

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_AnsichtSpalten_K:DSID';

-- insert FX_AnsichtSpalten datasets for FX_AnsichtTab_K
Insert into FX_ANSICHTSPALTEN_K (INDEXGRID,TABAKEY,SPALTEAKEY,INGRIDANZEIGEN,INGRIDLADEN,FILTER,ANZAHLNACHKOMMASTELLEN,BEARBHINZUFZUGELASSEN,BEARBZUGELASSEN,EINGABENOTWENDIG,ANSICHTSUCHEN,SUCHWERTAUSTABAKEY,SUCHWERTAUSSPALTEAKEY,WHERETABAKEY,WHERESPALTEAKEY,VERDECKENDURCHTABAKEY,VERDECKENDURCHSPALTEAKEY,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,ANSICHTDEFID,ANSICHTTABID,TABSPEIGID) values ('6','FX_AnsichtTab_K','Alias','-1','-1','-1',null,null,null,null,null,null,null,null,null,null,null,'MW',to_date('04.07.18','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_AnsichtSpalten_K:DSID'),
(select dsid from FX_AnsichtDef_K where ansicht = 'FX_AnsichtTab_K'),
(select dsid from FX_AnsichtTab_K where ansicht = 'FX_AnsichtTab_K' and jointyp = 'x'),
(select FX_TabSpEig_K.dsid from FX_TabSpEig_K join FX_TabDef_K on FX_TabSpEig_K.TABDEFID = FX_TabDef_K.DSID where FX_TabDef_K.tabelle = 'FX_AnsichtTab_K' and FX_TabSpEig_K.spalte = 'Alias'));

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_AnsichtSpalten_K:DSID';

Insert into FX_ANSICHTSPALTEN_K (INDEXGRID,TABAKEY,SPALTEAKEY,INGRIDANZEIGEN,INGRIDLADEN,FILTER,ANZAHLNACHKOMMASTELLEN,BEARBHINZUFZUGELASSEN,BEARBZUGELASSEN,EINGABENOTWENDIG,ANSICHTSUCHEN,SUCHWERTAUSTABAKEY,SUCHWERTAUSSPALTEAKEY,WHERETABAKEY,WHERESPALTEAKEY,VERDECKENDURCHTABAKEY,VERDECKENDURCHSPALTEAKEY,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,ANSICHTDEFID,ANSICHTTABID,TABSPEIGID) values ('1','FX_AnsichtTab_K','Ansicht','-1','-1','-1',null,null,null,null,null,null,null,null,null,null,null,'MW',to_date('04.07.18','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_AnsichtSpalten_K:DSID'),
(select dsid from FX_AnsichtDef_K where ansicht = 'FX_AnsichtTab_K'),
(select dsid from FX_AnsichtTab_K where ansicht = 'FX_AnsichtTab_K' and jointyp = 'x'),
(select FX_TabSpEig_K.dsid from FX_TabSpEig_K join FX_TabDef_K on FX_TabSpEig_K.TABDEFID = FX_TabDef_K.DSID where FX_TabDef_K.tabelle = 'FX_AnsichtTab_K' and FX_TabSpEig_K.spalte = 'Ansicht'));

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_AnsichtSpalten_K:DSID';

Insert into FX_ANSICHTSPALTEN_K (INDEXGRID,TABAKEY,SPALTEAKEY,INGRIDANZEIGEN,INGRIDLADEN,FILTER,ANZAHLNACHKOMMASTELLEN,BEARBHINZUFZUGELASSEN,BEARBZUGELASSEN,EINGABENOTWENDIG,ANSICHTSUCHEN,SUCHWERTAUSTABAKEY,SUCHWERTAUSSPALTEAKEY,WHERETABAKEY,WHERESPALTEAKEY,VERDECKENDURCHTABAKEY,VERDECKENDURCHSPALTEAKEY,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,ANSICHTDEFID,ANSICHTTABID,TABSPEIGID) values ('7','FX_AnsichtTab_K','BearbeitenZugelassen','-1','-1','-1',null,null,null,null,null,null,null,null,null,null,null,'MW',to_date('04.07.18','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_AnsichtSpalten_K:DSID'),
(select dsid from FX_AnsichtDef_K where ansicht = 'FX_AnsichtTab_K'),
(select dsid from FX_AnsichtTab_K where ansicht = 'FX_AnsichtTab_K' and jointyp = 'x'),
(select FX_TabSpEig_K.dsid from FX_TabSpEig_K join FX_TabDef_K on FX_TabSpEig_K.TABDEFID = FX_TabDef_K.DSID where FX_TabDef_K.tabelle = 'FX_AnsichtTab_K' and FX_TabSpEig_K.spalte = 'BearbeitenZugelassen'));

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_AnsichtSpalten_K:DSID';

Insert into FX_ANSICHTSPALTEN_K (INDEXGRID,TABAKEY,SPALTEAKEY,INGRIDANZEIGEN,INGRIDLADEN,FILTER,ANZAHLNACHKOMMASTELLEN,BEARBHINZUFZUGELASSEN,BEARBZUGELASSEN,EINGABENOTWENDIG,ANSICHTSUCHEN,SUCHWERTAUSTABAKEY,SUCHWERTAUSSPALTEAKEY,WHERETABAKEY,WHERESPALTEAKEY,VERDECKENDURCHTABAKEY,VERDECKENDURCHSPALTEAKEY,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,ANSICHTDEFID,ANSICHTTABID,TABSPEIGID) values ('14','FX_AnsichtTab_K','DSID','-1','-1','-1',null,null,null,null,null,null,null,null,null,null,null,'MW',to_date('04.07.18','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_AnsichtSpalten_K:DSID'),
(select dsid from FX_AnsichtDef_K where ansicht = 'FX_AnsichtTab_K'),
(select dsid from FX_AnsichtTab_K where ansicht = 'FX_AnsichtTab_K' and jointyp = 'x'),
(select FX_TabSpEig_K.dsid from FX_TabSpEig_K join FX_TabDef_K on FX_TabSpEig_K.TABDEFID = FX_TabDef_K.DSID where FX_TabDef_K.tabelle = 'FX_AnsichtTab_K' and FX_TabSpEig_K.spalte = 'DSID'));

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_AnsichtSpalten_K:DSID';

Insert into FX_ANSICHTSPALTEN_K (INDEXGRID,TABAKEY,SPALTEAKEY,INGRIDANZEIGEN,INGRIDLADEN,FILTER,ANZAHLNACHKOMMASTELLEN,BEARBHINZUFZUGELASSEN,BEARBZUGELASSEN,EINGABENOTWENDIG,ANSICHTSUCHEN,SUCHWERTAUSTABAKEY,SUCHWERTAUSSPALTEAKEY,WHERETABAKEY,WHERESPALTEAKEY,VERDECKENDURCHTABAKEY,VERDECKENDURCHSPALTEAKEY,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,ANSICHTDEFID,ANSICHTTABID,TABSPEIGID) values ('11','FX_AnsichtTab_K','Join1SpalteAKey','-1','-1','-1',null,null,null,null,null,null,null,null,null,null,null,'MW',to_date('04.07.18','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_AnsichtSpalten_K:DSID'),
(select dsid from FX_AnsichtDef_K where ansicht = 'FX_AnsichtTab_K'),
(select dsid from FX_AnsichtTab_K where ansicht = 'FX_AnsichtTab_K' and jointyp = 'x'),
(select FX_TabSpEig_K.dsid from FX_TabSpEig_K join FX_TabDef_K on FX_TabSpEig_K.TABDEFID = FX_TabDef_K.DSID where FX_TabDef_K.tabelle = 'FX_AnsichtTab_K' and FX_TabSpEig_K.spalte = 'Join1SpalteAKey'));

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_AnsichtSpalten_K:DSID';

Insert into FX_ANSICHTSPALTEN_K (INDEXGRID,TABAKEY,SPALTEAKEY,INGRIDANZEIGEN,INGRIDLADEN,FILTER,ANZAHLNACHKOMMASTELLEN,BEARBHINZUFZUGELASSEN,BEARBZUGELASSEN,EINGABENOTWENDIG,ANSICHTSUCHEN,SUCHWERTAUSTABAKEY,SUCHWERTAUSSPALTEAKEY,WHERETABAKEY,WHERESPALTEAKEY,VERDECKENDURCHTABAKEY,VERDECKENDURCHSPALTEAKEY,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,ANSICHTDEFID,ANSICHTTABID,TABSPEIGID) values ('13','FX_AnsichtTab_K','Join2SpalteAKey','-1','-1','-1',null,null,null,null,null,null,null,null,null,null,null,'MW',to_date('04.07.18','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_AnsichtSpalten_K:DSID'),
(select dsid from FX_AnsichtDef_K where ansicht = 'FX_AnsichtTab_K'),
(select dsid from FX_AnsichtTab_K where ansicht = 'FX_AnsichtTab_K' and jointyp = 'x'),
(select FX_TabSpEig_K.dsid from FX_TabSpEig_K join FX_TabDef_K on FX_TabSpEig_K.TABDEFID = FX_TabDef_K.DSID where FX_TabDef_K.tabelle = 'FX_AnsichtTab_K' and FX_TabSpEig_K.spalte = 'Join2SpalteAKey'));

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_AnsichtSpalten_K:DSID';

Insert into FX_ANSICHTSPALTEN_K (INDEXGRID,TABAKEY,SPALTEAKEY,INGRIDANZEIGEN,INGRIDLADEN,FILTER,ANZAHLNACHKOMMASTELLEN,BEARBHINZUFZUGELASSEN,BEARBZUGELASSEN,EINGABENOTWENDIG,ANSICHTSUCHEN,SUCHWERTAUSTABAKEY,SUCHWERTAUSSPALTEAKEY,WHERETABAKEY,WHERESPALTEAKEY,VERDECKENDURCHTABAKEY,VERDECKENDURCHSPALTEAKEY,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,ANSICHTDEFID,ANSICHTTABID,TABSPEIGID) values ('12','FX_AnsichtTab_K','Join2TabAKey','-1','-1','-1',null,null,null,null,null,null,null,null,null,null,null,'MW',to_date('04.07.18','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_AnsichtSpalten_K:DSID'),
(select dsid from FX_AnsichtDef_K where ansicht = 'FX_AnsichtTab_K'),
(select dsid from FX_AnsichtTab_K where ansicht = 'FX_AnsichtTab_K' and jointyp = 'x'),
(select FX_TabSpEig_K.dsid from FX_TabSpEig_K join FX_TabDef_K on FX_TabSpEig_K.TABDEFID = FX_TabDef_K.DSID where FX_TabDef_K.tabelle = 'FX_AnsichtTab_K' and FX_TabSpEig_K.spalte = 'Join2TabAKey'));

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_AnsichtSpalten_K:DSID';

Insert into FX_ANSICHTSPALTEN_K (INDEXGRID,TABAKEY,SPALTEAKEY,INGRIDANZEIGEN,INGRIDLADEN,FILTER,ANZAHLNACHKOMMASTELLEN,BEARBHINZUFZUGELASSEN,BEARBZUGELASSEN,EINGABENOTWENDIG,ANSICHTSUCHEN,SUCHWERTAUSTABAKEY,SUCHWERTAUSSPALTEAKEY,WHERETABAKEY,WHERESPALTEAKEY,VERDECKENDURCHTABAKEY,VERDECKENDURCHSPALTEAKEY,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,ANSICHTDEFID,ANSICHTTABID,TABSPEIGID) values ('10','FX_AnsichtTab_K','JoinTyp','-1','-1','-1',null,null,null,null,null,null,null,null,null,null,null,'MW',to_date('04.07.18','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_AnsichtSpalten_K:DSID'),
(select dsid from FX_AnsichtDef_K where ansicht = 'FX_AnsichtTab_K'),
(select dsid from FX_AnsichtTab_K where ansicht = 'FX_AnsichtTab_K' and jointyp = 'x'),
(select FX_TabSpEig_K.dsid from FX_TabSpEig_K join FX_TabDef_K on FX_TabSpEig_K.TABDEFID = FX_TabDef_K.DSID where FX_TabDef_K.tabelle = 'FX_AnsichtTab_K' and FX_TabSpEig_K.spalte = 'JoinTyp'));

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_AnsichtSpalten_K:DSID';

Insert into FX_ANSICHTSPALTEN_K (INDEXGRID,TABAKEY,SPALTEAKEY,INGRIDANZEIGEN,INGRIDLADEN,FILTER,ANZAHLNACHKOMMASTELLEN,BEARBHINZUFZUGELASSEN,BEARBZUGELASSEN,EINGABENOTWENDIG,ANSICHTSUCHEN,SUCHWERTAUSTABAKEY,SUCHWERTAUSSPALTEAKEY,WHERETABAKEY,WHERESPALTEAKEY,VERDECKENDURCHTABAKEY,VERDECKENDURCHSPALTEAKEY,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,ANSICHTDEFID,ANSICHTTABID,TABSPEIGID) values ('2','FX_AnsichtTab_K','Reihenfolge','-1','-1','-1',null,null,null,null,null,null,null,null,null,null,null,'MW',to_date('04.07.18','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_AnsichtSpalten_K:DSID'),
(select dsid from FX_AnsichtDef_K where ansicht = 'FX_AnsichtTab_K'),
(select dsid from FX_AnsichtTab_K where ansicht = 'FX_AnsichtTab_K' and jointyp = 'x'),
(select FX_TabSpEig_K.dsid from FX_TabSpEig_K join FX_TabDef_K on FX_TabSpEig_K.TABDEFID = FX_TabDef_K.DSID where FX_TabDef_K.tabelle = 'FX_AnsichtTab_K' and FX_TabSpEig_K.spalte = 'Reihenfolge'));

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_AnsichtSpalten_K:DSID';

Insert into FX_ANSICHTSPALTEN_K (INDEXGRID,TABAKEY,SPALTEAKEY,INGRIDANZEIGEN,INGRIDLADEN,FILTER,ANZAHLNACHKOMMASTELLEN,BEARBHINZUFZUGELASSEN,BEARBZUGELASSEN,EINGABENOTWENDIG,ANSICHTSUCHEN,SUCHWERTAUSTABAKEY,SUCHWERTAUSSPALTEAKEY,WHERETABAKEY,WHERESPALTEAKEY,VERDECKENDURCHTABAKEY,VERDECKENDURCHSPALTEAKEY,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,ANSICHTDEFID,ANSICHTTABID,TABSPEIGID) values ('18','FX_AnsichtTab_K','System','-1','-1','-1',null,null,null,null,null,null,null,null,null,null,null,'MW',to_date('04.07.18','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_AnsichtSpalten_K:DSID'),
(select dsid from FX_AnsichtDef_K where ansicht = 'FX_AnsichtTab_K'),
(select dsid from FX_AnsichtTab_K where ansicht = 'FX_AnsichtTab_K' and jointyp = 'x'),
(select FX_TabSpEig_K.dsid from FX_TabSpEig_K join FX_TabDef_K on FX_TabSpEig_K.TABDEFID = FX_TabDef_K.DSID where FX_TabDef_K.tabelle = 'FX_AnsichtTab_K' and FX_TabSpEig_K.spalte = 'System'));

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_AnsichtSpalten_K:DSID';

Insert into FX_ANSICHTSPALTEN_K (INDEXGRID,TABAKEY,SPALTEAKEY,INGRIDANZEIGEN,INGRIDLADEN,FILTER,ANZAHLNACHKOMMASTELLEN,BEARBHINZUFZUGELASSEN,BEARBZUGELASSEN,EINGABENOTWENDIG,ANSICHTSUCHEN,SUCHWERTAUSTABAKEY,SUCHWERTAUSSPALTEAKEY,WHERETABAKEY,WHERESPALTEAKEY,VERDECKENDURCHTABAKEY,VERDECKENDURCHSPALTEAKEY,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,ANSICHTDEFID,ANSICHTTABID,TABSPEIGID) values ('3','FX_AnsichtTab_K','TabAkey','-1','-1','-1',null,null,null,null,null,null,null,null,null,null,null,'MW',to_date('04.07.18','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_AnsichtSpalten_K:DSID'),
(select dsid from FX_AnsichtDef_K where ansicht = 'FX_AnsichtTab_K'),
(select dsid from FX_AnsichtTab_K where ansicht = 'FX_AnsichtTab_K' and jointyp = 'x'),
(select FX_TabSpEig_K.dsid from FX_TabSpEig_K join FX_TabDef_K on FX_TabSpEig_K.TABDEFID = FX_TabDef_K.DSID where FX_TabDef_K.tabelle = 'FX_AnsichtTab_K' and FX_TabSpEig_K.spalte = 'TabAkey'));

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_AnsichtSpalten_K:DSID';

Insert into FX_ANSICHTSPALTEN_K (INDEXGRID,TABAKEY,SPALTEAKEY,INGRIDANZEIGEN,INGRIDLADEN,FILTER,ANZAHLNACHKOMMASTELLEN,BEARBHINZUFZUGELASSEN,BEARBZUGELASSEN,EINGABENOTWENDIG,ANSICHTSUCHEN,SUCHWERTAUSTABAKEY,SUCHWERTAUSSPALTEAKEY,WHERETABAKEY,WHERESPALTEAKEY,VERDECKENDURCHTABAKEY,VERDECKENDURCHSPALTEAKEY,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,ANSICHTDEFID,ANSICHTTABID,TABSPEIGID) values ('4','FX_AnsichtTab_K','Tabelle','-1','-1','-1',null,null,null,null,null,null,null,null,null,null,null,'MW',to_date('04.07.18','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_AnsichtSpalten_K:DSID'),
(select dsid from FX_AnsichtDef_K where ansicht = 'FX_AnsichtTab_K'),
(select dsid from FX_AnsichtTab_K where ansicht = 'FX_AnsichtTab_K' and jointyp = 'x'),
(select FX_TabSpEig_K.dsid from FX_TabSpEig_K join FX_TabDef_K on FX_TabSpEig_K.TABDEFID = FX_TabDef_K.DSID where FX_TabDef_K.tabelle = 'FX_AnsichtTab_K' and FX_TabSpEig_K.spalte = 'Tabelle'));

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_AnsichtSpalten_K:DSID';

Insert into FX_ANSICHTSPALTEN_K (INDEXGRID,TABAKEY,SPALTEAKEY,INGRIDANZEIGEN,INGRIDLADEN,FILTER,ANZAHLNACHKOMMASTELLEN,BEARBHINZUFZUGELASSEN,BEARBZUGELASSEN,EINGABENOTWENDIG,ANSICHTSUCHEN,SUCHWERTAUSTABAKEY,SUCHWERTAUSSPALTEAKEY,WHERETABAKEY,WHERESPALTEAKEY,VERDECKENDURCHTABAKEY,VERDECKENDURCHSPALTEAKEY,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,ANSICHTDEFID,ANSICHTTABID,TABSPEIGID) values ('16','FX_AnsichtTab_K','angelegtAm','-1','-1','-1',null,null,null,null,null,null,null,null,null,null,null,'MW',to_date('04.07.18','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_AnsichtSpalten_K:DSID'),
(select dsid from FX_AnsichtDef_K where ansicht = 'FX_AnsichtTab_K'),
(select dsid from FX_AnsichtTab_K where ansicht = 'FX_AnsichtTab_K' and jointyp = 'x'),
(select FX_TabSpEig_K.dsid from FX_TabSpEig_K join FX_TabDef_K on FX_TabSpEig_K.TABDEFID = FX_TabDef_K.DSID where FX_TabDef_K.tabelle = 'FX_AnsichtTab_K' and FX_TabSpEig_K.spalte = 'angelegtAm'));

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_AnsichtSpalten_K:DSID';

Insert into FX_ANSICHTSPALTEN_K (INDEXGRID,TABAKEY,SPALTEAKEY,INGRIDANZEIGEN,INGRIDLADEN,FILTER,ANZAHLNACHKOMMASTELLEN,BEARBHINZUFZUGELASSEN,BEARBZUGELASSEN,EINGABENOTWENDIG,ANSICHTSUCHEN,SUCHWERTAUSTABAKEY,SUCHWERTAUSSPALTEAKEY,WHERETABAKEY,WHERESPALTEAKEY,VERDECKENDURCHTABAKEY,VERDECKENDURCHSPALTEAKEY,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,ANSICHTDEFID,ANSICHTTABID,TABSPEIGID) values ('15','FX_AnsichtTab_K','angelegtVon','-1','-1','-1',null,null,null,null,null,null,null,null,null,null,null,'MW',to_date('04.07.18','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_AnsichtSpalten_K:DSID'),
(select dsid from FX_AnsichtDef_K where ansicht = 'FX_AnsichtTab_K'),
(select dsid from FX_AnsichtTab_K where ansicht = 'FX_AnsichtTab_K' and jointyp = 'x'),
(select FX_TabSpEig_K.dsid from FX_TabSpEig_K join FX_TabDef_K on FX_TabSpEig_K.TABDEFID = FX_TabDef_K.DSID where FX_TabDef_K.tabelle = 'FX_AnsichtTab_K' and FX_TabSpEig_K.spalte = 'angelegtVon'));

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_AnsichtSpalten_K:DSID';

-- *********************************
-- insert FX_Menues datasets 

-- insert hautpmenue Administration, ebene 0
Insert into FX_MENUES_K (MENUEID,MENUE,ANZEIGENAME,TYP,EBENE,UNTERMENUEVON,LIZENZ,GRUPPE,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,URLPATH,BEREICHSID,AKTIV,ANSICHTDEFID,HAUPTMENUEID) 
values ('10','Admin','Administration','KNOTEN','0',null,null,'Administration','MW',to_date('04.07.18','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_Menues_K:DSID'),
null,(select b.BereichsID from BENUTZERBEREICHEDEF b where b.name = 'Administrator'),'-1',null,null);

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_Menues_K:DSID';

-- insert Untermenue AdminOberfläche, ebene 1
Insert into FX_MENUES_K (MENUEID,MENUE,ANZEIGENAME,TYP,EBENE,UNTERMENUEVON,LIZENZ,GRUPPE,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,URLPATH,BEREICHSID,AKTIV,ANSICHTDEFID,HAUPTMENUEID) 
values ('1001','AdminOberflaeche','Oberfläche','KNOTEN','1','Admin',null,null,'MW',to_date('04.07.18','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_Menues_K:DSID'),
null,(select b.BereichsID from BENUTZERBEREICHEDEF b where b.name = 'Administrator'),'-1',null,
(select dsid from FX_Menues_K where anzeigename = 'Administration'));

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_Menues_K:DSID';

-- insert Tabellen-Menues, ebene 2
Insert into FX_MENUES_K (MENUEID,MENUE,ANZEIGENAME,TYP,EBENE,UNTERMENUEVON,LIZENZ,GRUPPE,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,URLPATH,BEREICHSID,AKTIV,ANSICHTDEFID,HAUPTMENUEID) 
values ('100101','AdminTabellen','Tabellen','ANSICHT','2','AdminOberflaeche',null,null,'MW',to_date('04.07.18','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_Menues_K:DSID'),
'tabDef',
(select BereichsId from BenutzerBereicheDef where NAME = 'Administrator'),'-1',
(select dsid from FX_AnsichtDef_K where URLPATH = 'tabDef'),
(select dsid from FX_Menues_K where anzeigename = 'Oberfläche'));

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_Menues_K:DSID';

Insert into FX_MENUES_K (MENUEID,MENUE,ANZEIGENAME,TYP,EBENE,UNTERMENUEVON,LIZENZ,GRUPPE,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,URLPATH,BEREICHSID,AKTIV,ANSICHTDEFID,HAUPTMENUEID) 
values ('100102','AdminTabellenEig','Tabellen-Spalten','ANSICHT','2','AdminOberflaeche',null,null,'MW',to_date('04.07.18','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_Menues_K:DSID'),
'tabSpeig',
(select BereichsId from BenutzerBereicheDef where NAME = 'Administrator'),'-1',
(select dsid from FX_AnsichtDef_K where URLPATH = 'tabSpeig'),
(select dsid from FX_Menues_K where anzeigename = 'Oberfläche'));

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_Menues_K:DSID';

Insert into FX_MENUES_K (MENUEID,MENUE,ANZEIGENAME,TYP,EBENE,UNTERMENUEVON,LIZENZ,GRUPPE,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,URLPATH,BEREICHSID,AKTIV,ANSICHTDEFID,HAUPTMENUEID) 
values ('100130','AdminMenues','Menüs','ANSICHT','2','AdminOberflaeche',null,null,'MW',to_date('04.07.18','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_Menues_K:DSID'),
'menues',
(select BereichsId from BenutzerBereicheDef where NAME = 'Administrator'),'-1',
(select dsid from FX_AnsichtDef_K where URLPATH = 'menues'),
(select dsid from FX_Menues_K where anzeigename = 'Oberfläche'));

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_Menues_K:DSID';

Insert into FX_MENUES_K (MENUEID,MENUE,ANZEIGENAME,TYP,EBENE,UNTERMENUEVON,LIZENZ,GRUPPE,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,URLPATH,BEREICHSID,AKTIV,ANSICHTDEFID,HAUPTMENUEID) 
values ('100110','AdminAnsichten','Ansichten','ANSICHT','2','AdminOberflaeche',null,null,'MW',to_date('04.07.18','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_Menues_K:DSID'),
'ansichtDef',
(select BereichsId from BenutzerBereicheDef where NAME = 'Administrator'),'-1',
(select dsid from FX_AnsichtDef_K where URLPATH = 'ansichtDef'),
(select dsid from FX_Menues_K where anzeigename = 'Oberfläche'));

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_Menues_K:DSID';

Insert into FX_MENUES_K (MENUEID,MENUE,ANZEIGENAME,TYP,EBENE,UNTERMENUEVON,LIZENZ,GRUPPE,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,URLPATH,BEREICHSID,AKTIV,ANSICHTDEFID,HAUPTMENUEID) 
values ('100111','AdminAnsichtSpalten','Ansichtsspalten','ANSICHT','2','AdminOberflaeche',null,null,'MW',to_date('04.07.18','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_Menues_K:DSID'),
'ansichtSpalten',
(select BereichsId from BenutzerBereicheDef where NAME = 'Administrator'),'-1',
(select dsid from FX_AnsichtDef_K where URLPATH = 'ansichtSpalten'),
(select dsid from FX_Menues_K where anzeigename = 'Oberfläche'));

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_Menues_K:DSID';

-- insert FX_AnsichtSpalten datasets for Menues
Insert into FX_ANSICHTSPALTEN_K (INDEXGRID,TABAKEY,SPALTEAKEY,INGRIDANZEIGEN,INGRIDLADEN,FILTER,ANZAHLNACHKOMMASTELLEN,BEARBHINZUFZUGELASSEN,BEARBZUGELASSEN,EINGABENOTWENDIG,ANSICHTSUCHEN,SUCHWERTAUSTABAKEY,SUCHWERTAUSSPALTEAKEY,WHERETABAKEY,WHERESPALTEAKEY,VERDECKENDURCHTABAKEY,VERDECKENDURCHSPALTEAKEY,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,ANSICHTDEFID,ANSICHTTABID,TABSPEIGID) 
values ('26','FX_Menues_K','AnsichtDefId','-1','-1','-1',null,'-1','-1',null,'FX_AnsichtDef_K','FX_AnsichtDef_K','DSID',null,null,'FX_AnsichtDef_K','Ansicht','MW',to_date('04.07.18','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_AnsichtSpalten_K:DSID'),
(select dsid from FX_AnsichtDef_K where ansicht = 'FX_Menues_K'),
(select dsid from FX_AnsichtTab_K where ansicht = 'FX_Menues_K' and jointyp = 'x'),
(select FX_TabSpEig_K.dsid from FX_TabSpEig_K join FX_TabDef_K on FX_TabSpEig_K.TABDEFID = FX_TabDef_K.DSID where FX_TabDef_K.tabelle = 'FX_Menues_K' and FX_TabSpEig_K.spalte = 'AnsichtDefId'));

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_AnsichtSpalten_K:DSID';

Insert into FX_ANSICHTSPALTEN_K (INDEXGRID,TABAKEY,SPALTEAKEY,INGRIDANZEIGEN,INGRIDLADEN,FILTER,ANZAHLNACHKOMMASTELLEN,BEARBHINZUFZUGELASSEN,BEARBZUGELASSEN,EINGABENOTWENDIG,ANSICHTSUCHEN,SUCHWERTAUSTABAKEY,SUCHWERTAUSSPALTEAKEY,WHERETABAKEY,WHERESPALTEAKEY,VERDECKENDURCHTABAKEY,VERDECKENDURCHSPALTEAKEY,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,ANSICHTDEFID,ANSICHTTABID,TABSPEIGID) 
values ('3','FX_Menues_K','AnzeigeName','-1','-1','-1',null,null,null,null,null,null,null,null,null,null,null,'MW',to_date('04.07.18','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_AnsichtSpalten_K:DSID'),
(select dsid from FX_AnsichtDef_K where ansicht = 'FX_Menues_K'),
(select dsid from FX_AnsichtTab_K where ansicht = 'FX_Menues_K' and jointyp = 'x'),
(select FX_TabSpEig_K.dsid from FX_TabSpEig_K join FX_TabDef_K on FX_TabSpEig_K.TABDEFID = FX_TabDef_K.DSID where FX_TabDef_K.tabelle = 'FX_Menues_K' and FX_TabSpEig_K.spalte = 'AnzeigeName'));

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_AnsichtSpalten_K:DSID';

Insert into FX_ANSICHTSPALTEN_K (INDEXGRID,TABAKEY,SPALTEAKEY,INGRIDANZEIGEN,INGRIDLADEN,FILTER,ANZAHLNACHKOMMASTELLEN,BEARBHINZUFZUGELASSEN,BEARBZUGELASSEN,EINGABENOTWENDIG,ANSICHTSUCHEN,SUCHWERTAUSTABAKEY,SUCHWERTAUSSPALTEAKEY,WHERETABAKEY,WHERESPALTEAKEY,VERDECKENDURCHTABAKEY,VERDECKENDURCHSPALTEAKEY,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,ANSICHTDEFID,ANSICHTTABID,TABSPEIGID) 
values ('22','FX_Menues_K','BereichsId','-1','-1','-1',null,'-1','-1',null,'BenutzerBereicheDef','BenutzerBereicheDef','BereichsId',null,null,'BenutzerBereicheDef','Name','MW',to_date('04.07.18','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_AnsichtSpalten_K:DSID'),
(select dsid from FX_AnsichtDef_K where ansicht = 'FX_Menues_K'),
(select dsid from FX_AnsichtTab_K where ansicht = 'FX_Menues_K' and jointyp = 'x'),
(select FX_TabSpEig_K.dsid from FX_TabSpEig_K join FX_TabDef_K on FX_TabSpEig_K.TABDEFID = FX_TabDef_K.DSID where FX_TabDef_K.tabelle = 'FX_Menues_K' and FX_TabSpEig_K.spalte = 'BereichsId'));

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_AnsichtSpalten_K:DSID';

Insert into FX_ANSICHTSPALTEN_K (INDEXGRID,TABAKEY,SPALTEAKEY,INGRIDANZEIGEN,INGRIDLADEN,FILTER,ANZAHLNACHKOMMASTELLEN,BEARBHINZUFZUGELASSEN,BEARBZUGELASSEN,EINGABENOTWENDIG,ANSICHTSUCHEN,SUCHWERTAUSTABAKEY,SUCHWERTAUSSPALTEAKEY,WHERETABAKEY,WHERESPALTEAKEY,VERDECKENDURCHTABAKEY,VERDECKENDURCHSPALTEAKEY,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,ANSICHTDEFID,ANSICHTTABID,TABSPEIGID) 
values ('23','FX_Menues_K','DSID','-1','-1','-1',null,null,null,null,null,null,null,null,null,null,null,'MW',to_date('04.07.18','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_AnsichtSpalten_K:DSID'),
(select dsid from FX_AnsichtDef_K where ansicht = 'FX_Menues_K'),
(select dsid from FX_AnsichtTab_K where ansicht = 'FX_Menues_K' and jointyp = 'x'),
(select FX_TabSpEig_K.dsid from FX_TabSpEig_K join FX_TabDef_K on FX_TabSpEig_K.TABDEFID = FX_TabDef_K.DSID where FX_TabDef_K.tabelle = 'FX_Menues_K' and FX_TabSpEig_K.spalte = 'DSID'));

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_AnsichtSpalten_K:DSID';

Insert into FX_ANSICHTSPALTEN_K (INDEXGRID,TABAKEY,SPALTEAKEY,INGRIDANZEIGEN,INGRIDLADEN,FILTER,ANZAHLNACHKOMMASTELLEN,BEARBHINZUFZUGELASSEN,BEARBZUGELASSEN,EINGABENOTWENDIG,ANSICHTSUCHEN,SUCHWERTAUSTABAKEY,SUCHWERTAUSSPALTEAKEY,WHERETABAKEY,WHERESPALTEAKEY,VERDECKENDURCHTABAKEY,VERDECKENDURCHSPALTEAKEY,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,ANSICHTDEFID,ANSICHTTABID,TABSPEIGID) 
values ('5','FX_Menues_K','Ebene','-1','-1','-1',null,null,null,null,null,null,null,null,null,null,null,'MW',to_date('04.07.18','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_AnsichtSpalten_K:DSID'),
(select dsid from FX_AnsichtDef_K where ansicht = 'FX_Menues_K'),
(select dsid from FX_AnsichtTab_K where ansicht = 'FX_Menues_K' and jointyp = 'x'),
(select FX_TabSpEig_K.dsid from FX_TabSpEig_K join FX_TabDef_K on FX_TabSpEig_K.TABDEFID = FX_TabDef_K.DSID where FX_TabDef_K.tabelle = 'FX_Menues_K' and FX_TabSpEig_K.spalte = 'Ebene'));

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_AnsichtSpalten_K:DSID';

Insert into FX_ANSICHTSPALTEN_K (INDEXGRID,TABAKEY,SPALTEAKEY,INGRIDANZEIGEN,INGRIDLADEN,FILTER,ANZAHLNACHKOMMASTELLEN,BEARBHINZUFZUGELASSEN,BEARBZUGELASSEN,EINGABENOTWENDIG,ANSICHTSUCHEN,SUCHWERTAUSTABAKEY,SUCHWERTAUSSPALTEAKEY,WHERETABAKEY,WHERESPALTEAKEY,VERDECKENDURCHTABAKEY,VERDECKENDURCHSPALTEAKEY,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,ANSICHTDEFID,ANSICHTTABID,TABSPEIGID) 
values ('17','FX_Menues_K','Gruppe','-1','-1','-1',null,null,null,null,null,null,null,null,null,null,null,'MW',to_date('04.07.18','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_AnsichtSpalten_K:DSID'),
(select dsid from FX_AnsichtDef_K where ansicht = 'FX_Menues_K'),
(select dsid from FX_AnsichtTab_K where ansicht = 'FX_Menues_K' and jointyp = 'x'),
(select FX_TabSpEig_K.dsid from FX_TabSpEig_K join FX_TabDef_K on FX_TabSpEig_K.TABDEFID = FX_TabDef_K.DSID where FX_TabDef_K.tabelle = 'FX_Menues_K' and FX_TabSpEig_K.spalte = 'Gruppe'));

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_AnsichtSpalten_K:DSID';

Insert into FX_ANSICHTSPALTEN_K (INDEXGRID,TABAKEY,SPALTEAKEY,INGRIDANZEIGEN,INGRIDLADEN,FILTER,ANZAHLNACHKOMMASTELLEN,BEARBHINZUFZUGELASSEN,BEARBZUGELASSEN,EINGABENOTWENDIG,ANSICHTSUCHEN,SUCHWERTAUSTABAKEY,SUCHWERTAUSSPALTEAKEY,WHERETABAKEY,WHERESPALTEAKEY,VERDECKENDURCHTABAKEY,VERDECKENDURCHSPALTEAKEY,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,ANSICHTDEFID,ANSICHTTABID,TABSPEIGID) 
values ('27','FX_Menues_K','HauptMenueId','-1','-1','-1',null,'-1','-1',null,'FX_Menues_K','FX_Menues_K','DSID',null,null,'FX_Menues_K','Menue','MW',to_date('04.07.18','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_AnsichtSpalten_K:DSID'),
(select dsid from FX_AnsichtDef_K where ansicht = 'FX_Menues_K'),
(select dsid from FX_AnsichtTab_K where ansicht = 'FX_Menues_K' and jointyp = 'x'),
(select FX_TabSpEig_K.dsid from FX_TabSpEig_K join FX_TabDef_K on FX_TabSpEig_K.TABDEFID = FX_TabDef_K.DSID where FX_TabDef_K.tabelle = 'FX_Menues_K' and FX_TabSpEig_K.spalte = 'HauptMenueId'));

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_AnsichtSpalten_K:DSID';

Insert into FX_ANSICHTSPALTEN_K (INDEXGRID,TABAKEY,SPALTEAKEY,INGRIDANZEIGEN,INGRIDLADEN,FILTER,ANZAHLNACHKOMMASTELLEN,BEARBHINZUFZUGELASSEN,BEARBZUGELASSEN,EINGABENOTWENDIG,ANSICHTSUCHEN,SUCHWERTAUSTABAKEY,SUCHWERTAUSSPALTEAKEY,WHERETABAKEY,WHERESPALTEAKEY,VERDECKENDURCHTABAKEY,VERDECKENDURCHSPALTEAKEY,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,ANSICHTDEFID,ANSICHTTABID,TABSPEIGID) 
values ('10','FX_Menues_K','Lizenz','-1','-1','-1',null,null,null,null,null,null,null,null,null,null,null,'MW',to_date('04.07.18','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_AnsichtSpalten_K:DSID'),
(select dsid from FX_AnsichtDef_K where ansicht = 'FX_Menues_K'),
(select dsid from FX_AnsichtTab_K where ansicht = 'FX_Menues_K' and jointyp = 'x'),
(select FX_TabSpEig_K.dsid from FX_TabSpEig_K join FX_TabDef_K on FX_TabSpEig_K.TABDEFID = FX_TabDef_K.DSID where FX_TabDef_K.tabelle = 'FX_Menues_K' and FX_TabSpEig_K.spalte = 'Lizenz'));

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_AnsichtSpalten_K:DSID';

Insert into FX_ANSICHTSPALTEN_K (INDEXGRID,TABAKEY,SPALTEAKEY,INGRIDANZEIGEN,INGRIDLADEN,FILTER,ANZAHLNACHKOMMASTELLEN,BEARBHINZUFZUGELASSEN,BEARBZUGELASSEN,EINGABENOTWENDIG,ANSICHTSUCHEN,SUCHWERTAUSTABAKEY,SUCHWERTAUSSPALTEAKEY,WHERETABAKEY,WHERESPALTEAKEY,VERDECKENDURCHTABAKEY,VERDECKENDURCHSPALTEAKEY,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,ANSICHTDEFID,ANSICHTTABID,TABSPEIGID) 
values ('2','FX_Menues_K','Menue','-1','-1','-1',null,null,null,null,null,null,null,null,null,null,null,'MW',to_date('04.07.18','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_AnsichtSpalten_K:DSID'),
(select dsid from FX_AnsichtDef_K where ansicht = 'FX_Menues_K'),
(select dsid from FX_AnsichtTab_K where ansicht = 'FX_Menues_K' and jointyp = 'x'),
(select FX_TabSpEig_K.dsid from FX_TabSpEig_K join FX_TabDef_K on FX_TabSpEig_K.TABDEFID = FX_TabDef_K.DSID where FX_TabDef_K.tabelle = 'FX_Menues_K' and FX_TabSpEig_K.spalte = 'Menue'));

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_AnsichtSpalten_K:DSID';

Insert into FX_ANSICHTSPALTEN_K (INDEXGRID,TABAKEY,SPALTEAKEY,INGRIDANZEIGEN,INGRIDLADEN,FILTER,ANZAHLNACHKOMMASTELLEN,BEARBHINZUFZUGELASSEN,BEARBZUGELASSEN,EINGABENOTWENDIG,ANSICHTSUCHEN,SUCHWERTAUSTABAKEY,SUCHWERTAUSSPALTEAKEY,WHERETABAKEY,WHERESPALTEAKEY,VERDECKENDURCHTABAKEY,VERDECKENDURCHSPALTEAKEY,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,ANSICHTDEFID,ANSICHTTABID,TABSPEIGID) 
values ('1','FX_Menues_K','MenueID','-1','-1','-1',null,null,null,null,null,null,null,null,null,null,null,'MW',to_date('04.07.18','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_AnsichtSpalten_K:DSID'),
(select dsid from FX_AnsichtDef_K where ansicht = 'FX_Menues_K'),
(select dsid from FX_AnsichtTab_K where ansicht = 'FX_Menues_K' and jointyp = 'x'),
(select FX_TabSpEig_K.dsid from FX_TabSpEig_K join FX_TabDef_K on FX_TabSpEig_K.TABDEFID = FX_TabDef_K.DSID where FX_TabDef_K.tabelle = 'FX_Menues_K' and FX_TabSpEig_K.spalte = 'MenueID'));

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_AnsichtSpalten_K:DSID';

Insert into FX_ANSICHTSPALTEN_K (INDEXGRID,TABAKEY,SPALTEAKEY,INGRIDANZEIGEN,INGRIDLADEN,FILTER,ANZAHLNACHKOMMASTELLEN,BEARBHINZUFZUGELASSEN,BEARBZUGELASSEN,EINGABENOTWENDIG,ANSICHTSUCHEN,SUCHWERTAUSTABAKEY,SUCHWERTAUSSPALTEAKEY,WHERETABAKEY,WHERESPALTEAKEY,VERDECKENDURCHTABAKEY,VERDECKENDURCHSPALTEAKEY,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,ANSICHTDEFID,ANSICHTTABID,TABSPEIGID) 
values ('21','FX_Menues_K','System','-1','-1','-1',null,null,null,null,null,null,null,null,null,null,null,'MW',to_date('04.07.18','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_AnsichtSpalten_K:DSID'),
(select dsid from FX_AnsichtDef_K where ansicht = 'FX_Menues_K'),
(select dsid from FX_AnsichtTab_K where ansicht = 'FX_Menues_K' and jointyp = 'x'),
(select FX_TabSpEig_K.dsid from FX_TabSpEig_K join FX_TabDef_K on FX_TabSpEig_K.TABDEFID = FX_TabDef_K.DSID where FX_TabDef_K.tabelle = 'FX_Menues_K' and FX_TabSpEig_K.spalte = 'System'));

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_AnsichtSpalten_K:DSID';

Insert into FX_ANSICHTSPALTEN_K (INDEXGRID,TABAKEY,SPALTEAKEY,INGRIDANZEIGEN,INGRIDLADEN,FILTER,ANZAHLNACHKOMMASTELLEN,BEARBHINZUFZUGELASSEN,BEARBZUGELASSEN,EINGABENOTWENDIG,ANSICHTSUCHEN,SUCHWERTAUSTABAKEY,SUCHWERTAUSSPALTEAKEY,WHERETABAKEY,WHERESPALTEAKEY,VERDECKENDURCHTABAKEY,VERDECKENDURCHSPALTEAKEY,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,ANSICHTDEFID,ANSICHTTABID,TABSPEIGID) 
values ('4','FX_Menues_K','Typ','-1','-1','-1',null,null,null,null,null,null,null,null,null,null,null,'MW',to_date('04.07.18','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_AnsichtSpalten_K:DSID'),
(select dsid from FX_AnsichtDef_K where ansicht = 'FX_Menues_K'),
(select dsid from FX_AnsichtTab_K where ansicht = 'FX_Menues_K' and jointyp = 'x'),
(select FX_TabSpEig_K.dsid from FX_TabSpEig_K join FX_TabDef_K on FX_TabSpEig_K.TABDEFID = FX_TabDef_K.DSID where FX_TabDef_K.tabelle = 'FX_Menues_K' and FX_TabSpEig_K.spalte = 'Typ'));

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_AnsichtSpalten_K:DSID';

Insert into FX_ANSICHTSPALTEN_K (INDEXGRID,TABAKEY,SPALTEAKEY,INGRIDANZEIGEN,INGRIDLADEN,FILTER,ANZAHLNACHKOMMASTELLEN,BEARBHINZUFZUGELASSEN,BEARBZUGELASSEN,EINGABENOTWENDIG,ANSICHTSUCHEN,SUCHWERTAUSTABAKEY,SUCHWERTAUSSPALTEAKEY,WHERETABAKEY,WHERESPALTEAKEY,VERDECKENDURCHTABAKEY,VERDECKENDURCHSPALTEAKEY,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,ANSICHTDEFID,ANSICHTTABID,TABSPEIGID) 
values ('6','FX_Menues_K','UntermenueVon','-1','-1','-1',null,null,null,null,null,null,null,null,null,null,null,'MW',to_date('04.07.18','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_AnsichtSpalten_K:DSID'),
(select dsid from FX_AnsichtDef_K where ansicht = 'FX_Menues_K'),
(select dsid from FX_AnsichtTab_K where ansicht = 'FX_Menues_K' and jointyp = 'x'),
(select FX_TabSpEig_K.dsid from FX_TabSpEig_K join FX_TabDef_K on FX_TabSpEig_K.TABDEFID = FX_TabDef_K.DSID where FX_TabDef_K.tabelle = 'FX_Menues_K' and FX_TabSpEig_K.spalte = 'UntermenueVon'));

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_AnsichtSpalten_K:DSID';

Insert into FX_ANSICHTSPALTEN_K (INDEXGRID,TABAKEY,SPALTEAKEY,INGRIDANZEIGEN,INGRIDLADEN,FILTER,ANZAHLNACHKOMMASTELLEN,BEARBHINZUFZUGELASSEN,BEARBZUGELASSEN,EINGABENOTWENDIG,ANSICHTSUCHEN,SUCHWERTAUSTABAKEY,SUCHWERTAUSSPALTEAKEY,WHERETABAKEY,WHERESPALTEAKEY,VERDECKENDURCHTABAKEY,VERDECKENDURCHSPALTEAKEY,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,ANSICHTDEFID,ANSICHTTABID,TABSPEIGID) 
values ('25','FX_Menues_K','aktiv','-1','-1','-1',null,'-1','-1',null,null,null,null,null,null,null,null,'MW',to_date('04.07.18','DD.MM.RR'),'0',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_AnsichtSpalten_K:DSID'),
(select dsid from FX_AnsichtDef_K where ansicht = 'FX_Menues_K'),
(select dsid from FX_AnsichtTab_K where ansicht = 'FX_Menues_K' and jointyp = 'x'),
(select FX_TabSpEig_K.dsid from FX_TabSpEig_K join FX_TabDef_K on FX_TabSpEig_K.TABDEFID = FX_TabDef_K.DSID where FX_TabDef_K.tabelle = 'FX_Menues_K' and FX_TabSpEig_K.spalte = 'aktiv'));

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_AnsichtSpalten_K:DSID';

Insert into FX_ANSICHTSPALTEN_K (INDEXGRID,TABAKEY,SPALTEAKEY,INGRIDANZEIGEN,INGRIDLADEN,FILTER,ANZAHLNACHKOMMASTELLEN,BEARBHINZUFZUGELASSEN,BEARBZUGELASSEN,EINGABENOTWENDIG,ANSICHTSUCHEN,SUCHWERTAUSTABAKEY,SUCHWERTAUSSPALTEAKEY,WHERETABAKEY,WHERESPALTEAKEY,VERDECKENDURCHTABAKEY,VERDECKENDURCHSPALTEAKEY,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,ANSICHTDEFID,ANSICHTTABID,TABSPEIGID) 
values ('19','FX_Menues_K','angelegtAm','-1','-1','-1',null,null,null,null,null,null,null,null,null,null,null,'MW',to_date('04.07.18','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_AnsichtSpalten_K:DSID'),
(select dsid from FX_AnsichtDef_K where ansicht = 'FX_Menues_K'),
(select dsid from FX_AnsichtTab_K where ansicht = 'FX_Menues_K' and jointyp = 'x'),
(select FX_TabSpEig_K.dsid from FX_TabSpEig_K join FX_TabDef_K on FX_TabSpEig_K.TABDEFID = FX_TabDef_K.DSID where FX_TabDef_K.tabelle = 'FX_Menues_K' and FX_TabSpEig_K.spalte = 'angelegtAm'));

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_AnsichtSpalten_K:DSID';

Insert into FX_ANSICHTSPALTEN_K (INDEXGRID,TABAKEY,SPALTEAKEY,INGRIDANZEIGEN,INGRIDLADEN,FILTER,ANZAHLNACHKOMMASTELLEN,BEARBHINZUFZUGELASSEN,BEARBZUGELASSEN,EINGABENOTWENDIG,ANSICHTSUCHEN,SUCHWERTAUSTABAKEY,SUCHWERTAUSSPALTEAKEY,WHERETABAKEY,WHERESPALTEAKEY,VERDECKENDURCHTABAKEY,VERDECKENDURCHSPALTEAKEY,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,ANSICHTDEFID,ANSICHTTABID,TABSPEIGID) 
values ('18','FX_Menues_K','angelegtVon','-1','-1','-1',null,null,null,null,null,null,null,null,null,null,null,'MW',to_date('04.07.18','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_AnsichtSpalten_K:DSID'),
(select dsid from FX_AnsichtDef_K where ansicht = 'FX_Menues_K'),
(select dsid from FX_AnsichtTab_K where ansicht = 'FX_Menues_K' and jointyp = 'x'),
(select FX_TabSpEig_K.dsid from FX_TabSpEig_K join FX_TabDef_K on FX_TabSpEig_K.TABDEFID = FX_TabDef_K.DSID where FX_TabDef_K.tabelle = 'FX_Menues_K' and FX_TabSpEig_K.spalte = 'angelegtVon'));

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_AnsichtSpalten_K:DSID';

Insert into FX_ANSICHTSPALTEN_K (INDEXGRID,TABAKEY,SPALTEAKEY,INGRIDANZEIGEN,INGRIDLADEN,FILTER,ANZAHLNACHKOMMASTELLEN,BEARBHINZUFZUGELASSEN,BEARBZUGELASSEN,EINGABENOTWENDIG,ANSICHTSUCHEN,SUCHWERTAUSTABAKEY,SUCHWERTAUSSPALTEAKEY,WHERETABAKEY,WHERESPALTEAKEY,VERDECKENDURCHTABAKEY,VERDECKENDURCHSPALTEAKEY,ANGELEGTVON,ANGELEGTAM,SYSTEM,DSID,ANSICHTDEFID,ANSICHTTABID,TABSPEIGID) 
values ('24','FX_Menues_K','urlPath','-1','-1','-1',null,null,null,null,null,null,null,null,null,null,null,'MW',to_date('04.07.18','DD.MM.RR'),'-1',
(select s.letztebelegtenr + s.inkrement from SysSequenz s where s.name = 'FX_AnsichtSpalten_K:DSID'),
(select dsid from FX_AnsichtDef_K where ansicht = 'FX_Menues_K'),
(select dsid from FX_AnsichtTab_K where ansicht = 'FX_Menues_K' and jointyp = 'x'),
(select FX_TabSpEig_K.dsid from FX_TabSpEig_K join FX_TabDef_K on FX_TabSpEig_K.TABDEFID = FX_TabDef_K.DSID where FX_TabDef_K.tabelle = 'FX_Menues_K' and FX_TabSpEig_K.spalte = 'urlPath'));

update SysSequenz set letztebelegtenr = (letztebelegtenr + inkrement) where name = 'FX_AnsichtSpalten_K:DSID';

-- fix bereich of menues of "Administrator"
update FX_Menues_K 
set Bereich = 'Administrator',
Bereichsid = (select b.BereichsID from BenutzerBereicheDef b where b.name = 'Administrator')
where menue = 'AdminOberflaeche';

update FX_Menues_K 
set Bereich = 'Administrator',
Bereichsid = (select b.BereichsID from BenutzerBereicheDef b where b.name = 'Administrator')
where menue = 'Admin';

commit;