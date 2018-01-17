

-- Tabelle FX_TabSpeig_K
update FX_TabSpeig_K set bearbErlaubt = -1, angelegtVon = angelegtVon || '#migration#' where bearbErlaubt is null;

update FX_TabSpeig_K set ps = 0, angelegtVon = angelegtVon || '#migration#' where ps is null;

update FX_TabSpeig_K set systemwert = 0, angelegtVon = angelegtVon || '#migration#' where systemwert is null;

-- Großschreibweise der DB-Datentypen in FX_TabSpeig_K -> TabSpeig.java
update FX_TabSpeig_K set dbdatentyp = upper(dbdatentyp), angelegtVon = angelegtVon || '#migration#';

-- Tabelle FX_TabDef_K
--  Feld Datenbank vereinheitlichen
update fx_tabdef_k set datenbank = 'K' where datenbank = 'k';
update fx_tabdef_k set datenbank = 'X' where datenbank = 'x';

-- Tabelle FX_TabSpeig_K
-- Feld Tabelle einheitliche Schreibweise
update fx_tabspeig_k set tabelle = 'BenutzerBereicheDef' where tabelle = 'BenutzerBereichedef';
update fx_tabspeig_k set tabelle = 'BenutzerDef' where tabelle = 'Benutzerdef';
update fx_tabspeig_k set tabelle = 'BenutzerRechte' where tabelle = 'Benutzerrechte';
update fx_tabspeig_k set tabelle = 'FN_KuGDef' where tabelle = 'FN_KUGDef';
update fx_tabspeig_k set tabelle = 'FN_RegelZonenDef' where tabelle = 'FN_RegelzonenDef';
update fx_tabspeig_k set tabelle = 'FN_Zp2KuG' where tabelle = 'FN_ZP2KuG';
update fx_tabspeig_k set tabelle = 'FO_FibuKontenDef' where tabelle = 'FO_FibukontenDef';
update fx_tabspeig_k set tabelle = 'FO_ObjektDatAt' where tabelle = 'FO_ObjektDatAT';

-- update BenutzerBereiche mit Filter importId is null
update BenutzerBereicheDef set ImportID = 10001471 where Name in ('RB_FO_KKKVorg2KKKVorgH', 'RB_FO_KKKVorg2BankUebertH');

-- update Spalte Zeittyp von FX_TabDef_K f�r Entity TabDef.ZEITTYP
update FX_TabDef_K set zeittyp = 'gueltigVon' where zeittyp = 'GueltigVon';

update FX_TabDef_K set zeittyp = 'vonBis' where zeittyp = 'VonBis';

update FX_TabDef_K set zeittyp = 'vonBis' where zeittyp = 'vonbis';

update FX_TabDef_K set zeittyp = 'datum' where zeittyp = 'Datum';

update FX_TabDef_K set zeittyp = 'VONBIS' where zeittyp = 'Vonbis';

update FX_TabDef_K set zeittyp = 'GUELTIGVON' where zeittyp = 'gueltigVon';

update FX_TabDef_K set zeittyp = 'VONBIS' where zeittyp = 'vonBis';

update FX_TabDef_K set zeittyp = 'DATUM' where zeittyp = 'datum';

-- FX_TabBez_K: Beziehung zwischen FX_TabDef_K und BenutzerBereicheDef
insert into FX_TabBez_K(tabelledef, defschluessel, tabelleeig, eigschluessel, operator, indb,
                        angelegtvon, angelegtam, ofdb, system, DSID)
values('BenutzerBereicheDef', 'Name', 'FX_TabDef_K', 'Bereich', '=', 0, 
                        '#migration#', to_date('14.04.2011', 'dd.mm.yyyy'), 'OF', -1, 
                        (select max(t.DSID) + 1 from FX_TabBez_K t )  ); 
          
-- FX_TabBez_K: Beziehung zwischen FX_TabSpeig_K und FX_TabDef_K
insert into FX_TabBez_K(tabelledef, defschluessel, tabelleeig, eigschluessel, operator, indb,
                        angelegtvon, angelegtam, ofdb, system, DSID)
values('FX_TabDef_K', 'Tabelle', 'FX_TabSpeig_K', 'Tabelle', '=', 0, 
                        '#migration#', to_date('08.05.2011', 'dd.mm.yyyy'), 'OF', -1, 
                        (select max(t.DSID) + 1 from FX_TabBez_K t )  );               
                        
-- set DBDATENTYP from STRING to ENUM for relevant TabSpeigs
update FX_TabSpeig_K set DBDATENTYP = 'ENUM' where tabelle = 'FX_TabDef_K' and spalte = 'DatenBank';

update FX_TabSpeig_K set DBDATENTYP = 'ENUM' where tabelle = 'FX_TabDef_K' and spalte = 'ZeitTyp';
                        
-- set flag bearberlaubt                        
update FX_TabSpeig_K set bearberlaubt = 0 where Tabelle = 'FX_TabDef_K' and spalte = 'Tabelle';
                        
-- Schreibweise BenutzerBereicheDef
update FX_TabDef_K set tabelle = 'BenutzerBereicheDef' where tabelle = 'BenutzerBereichedef';

-- add constraint from FX_TabSpeig_K to FX_TabDef_K, dazu einige doppelte Einträge aus FX_TabDef_K entfernen (z.B. ObjektKontakte)
alter table FX_TabSpeig_K add TabDefId number(11,0);

-- add constraint to FX_TabDef_K
alter table FX_TabSpeig_K
add	 CONSTRAINT "FK_FX_TabSpeig_TABDEFID" FOREIGN KEY ("TABDEFID")
	  REFERENCES "KD_RRE_PROD"."FX_TABDEF_K" ("DSID") ENABLE; 

-- delete duplicated entries in FX_TabDef_K
delete from FX_TabDef_K where tabelle = 'ObjektKontakte' and OFDB = 'X';
delete from FX_TabDef_K where tabelle = 'RegStrukStaatDef' and OFDB = 'OF';

-- set equal name of Tabelle in FX_TabSpEig_K and FX_TabDef_K
update FX_TabSpeig_K ts
set ts.tabelle = (select distinct t.tabelle from FX_TabDef_K t
                   where upper(t.tabelle)  = upper(ts.tabelle))
where ts.Tabelle not in (select distinct tabelle from FX_TabDef_K t2);
                     
-- copy all TabDefIDs from FX_TabDef_K to FX_TabSpEig_K                        
update FX_TabSpeig_K ts
set ts.TabDefId = (select distinct t.DSID 
                     from FX_TabDef_K t
                    where t.tabelle = ts.tabelle
                      )
where ts.TabDefId is null;

-- insert field TabDefId in FX_TabSpEig_K
INSERT INTO FX_TabSpEig_K(Tabelle, Reihenfolge, Spalte, Spaltenkopf, DBDatentyp, PS, Eindeutig, EingabeNotwendig, 
  BearbErlaubt, SystemWert, DefaultWert, Minimum, Maximum, TooltipText, AngelegtVon, AngelegtAm, Ofdb, System, 
  TabDefId, DSID)
VALUES('FX_TabSpEig_K', 28, 'TabDefId', 'TabDef_ID', 'LONGINTEGER', -1, null, -1, 0, 0, null, null, null, 
  'ID der Tabelle FX_TabDef_K', 'MW#migration#', '29.11.11', 'X', -1, 
  (select tt.dsid from FX_TabDef_K tt where tt.tabelle = 'FX_TabSpEig_K' and tt.ofdb = 'X'), 
  (select max(t.DSID) + 1 from FX_TabSpEig_K t) );
  
-- insert TabDefId into FX_AnsichtSpalten_K
INSERT INTO FX_AnsichtSpalten_K(Ansicht, IndexGrid, TabAKey, SpalteAKey, InGridAnzeigen, InGridLaden, Filter,
  bearbeitenIn, IndexBearb, BearbHinzufZugelassen, BearbZugelassen, AnsichtSuchen, 
  SuchwertAusTabAKey, SuchwertAusSpalteAKey, VerdeckenDurchTabAKey, VerdeckenDurchSpalteAKey, 
  AngelegtVon, AngelegtAm, Ofdb, System, DSID, TabASpalteA)
VALUES('FX_TabSpEig_K', 28, 'FX_TabSpEig_K', 'TabDefId', null, -1, -1, 'dcl', null, -1, null, 
  'FX_TabDef_K', 'FX_TabDef_K', 'Tabelle', 'FX_TabDef_K', 'Tabelle', 'MW#migration#', '29.11.2011', 
  'X', -1, (select max(a.DSID)+1 from FX_AnsichtSpalten_K a), 'FX_AnsichtSpalten_K:TabDefId' );  
  
-- set BOOLEAN-Type for field FX_TabSpeig_K:BearbErlaubt
update FX_TabSpEig_K set dbdatentyp = 'BOOLEAN' where tabelle = 'FX_TabSpEig_K' and spalte  = 'BearbErlaubt';

-- update FX_TabSpeig_K:EingabeNotwendig: make it to Boolean, before: value -2 for autofill FS with ID
update FX_TabSpeig_K set EingabeNotwendig = -1 where EingabeNotwendig in (-2,3);
update FX_TabSpeig_K set EingabeNotwendig = 0 where EingabeNotwendig is null;
update FX_TabSpeig_K set dbdatentyp = 'BOOLEAN' where tabelle = 'FX_TabSpEig_K' and spalte = 'EingabeNotwendig';
update FX_TabSpeig_K set dbdatentyp = 'BOOLEAN' where tabelle = 'FX_TabSpEig_K' and spalte = 'PS';
update FX_TabSpeig_K set dbdatentyp = 'BOOLEAN' where tabelle = 'FX_TabSpEig_K' and spalte = 'SystemWert';

update FX_TabSpeig_K set dbdatentyp = 'ENUM' where tabelle = 'FX_TabSpEig_K' and spalte = 'DBDatenTyp';

-- fehlende BenutzerBereiche eintragen
INSERT INTO BenutzerBereicheDef (abrechnen, bearbeiten, berechnen, exportieren, hinzufuegen, importieren, lesen,
  loeschen, stornieren, vergebbar, Beschreibung, name, bereichsid, unterschrift1, unterschrift2, unterschrift3, 
  alleds, verschluesseln, nameGB, adminautozuweis, angelegtvon, angelegtam, transaktionsid)   
VALUES (0, 0, 0, 0, 0, 0, -1, 0, 0, 0, 'Kraftwerke', 'Kraftwerke', 
  (select max(b2.bereichsid) + 1 from BenutzerBereicheDef b2), 
  0, 0, 0, -1, 0, 'KRAFTWERKE', -1, 0, '06.01.2012', 0);
  
INSERT INTO BenutzerBereicheDef (abrechnen, bearbeiten, berechnen, exportieren, hinzufuegen, importieren, lesen,
  loeschen, stornieren, vergebbar, Beschreibung, name, bereichsid, unterschrift1, unterschrift2, unterschrift3, 
  alleds, verschluesseln, nameGB, adminautozuweis, angelegtvon, angelegtam, transaktionsid)   
VALUES (0, 0, 0, 0, 0, 0, -1, 0, 0, 0, 'Projektleiter', 'Projektleiter', 
  (select max(b2.bereichsid) + 1 from BenutzerBereicheDef b2), 
  0, 0, 0, -1, 0, 'PROJEKTLEITER', -1, 0, '06.01.2012', 0);

-- alle Bereich-Namen in FX_TabDef_K auf Gross/Kleinschreibung hin anpassen, die von der Gross/Kleinschreibung in BerenutzerBereicheDef abweichen
update FX_TabDef_K ts
set ts.Bereich = (select distinct b.Name from BenutzerBereicheDef b
                   where upper(b.name)  = upper(ts.Bereich))
where upper(ts.Bereich)  in (select distinct upper(b2.Name) from BenutzerBereicheDef b2
                   where upper(b2.name)  = upper(ts.Bereich));

-- FX_MENUES_K
update FX_MENUES_K set typ = 'Knoten' where typ = 'knoten';

update FX_MENUES_K set typ = 'KNOTEN' where typ = 'Knoten';
update FX_MENUES_K set typ = 'AKTION' where typ = 'Aktion';
update FX_MENUES_K set typ = 'LADEN' where typ = 'Laden';
update FX_MENUES_K set typ = 'ANSICHT' where typ = 'Ansicht';

-- repair ebenen of FX_MENUES_K
-- ebene 1
update FX_MENUES_K menue1 set menue1.ebene = 1 
where exists(
  select *
  from FX_MENUES_K menue0 
  where menue0.menue = menue1.untermenuevon
  and menue0.ebene = 0 and (menue1.ebene <> 1 or menue1.ebene is null)
);

-- ebene 2
update FX_MENUES_K menue1 set menue1.ebene = 2 
where exists(
  select *
  from FX_MENUES_K menue0 
  where menue0.menue = menue1.untermenuevon
  and menue0.ebene = 1 and (menue1.ebene <> 2 or menue1.ebene is null)
);

-- ebene 3
update FX_MENUES_K menue1 set menue1.ebene = 3 
where exists(
  select *
  from FX_MENUES_K menue0 
  where menue0.menue = menue1.untermenuevon
  and menue0.ebene = 2 and (menue1.ebene <> 3 or menue1.ebene is null)
);

-- Tabelle FX_MENUES_K befüllen mit Tabellen, Tabellen-Spalten
insert into FX_MENUES_K(menueid, menue, anzeigename, typ, ebene, untermenuevon, 
				  angelegtvon, angelegtam, ofdb, system, DSID)
				  values(841903, 'AdminTabellen', 'Tabellen', 'ANSICHT', 2, 'AdminOberflaeche', '#migration#',
                  '24.02.2012', 'X', -1, (select max(t.DSID) + 1 from FX_MENUES_K t ) );
                  
insert into FX_MENUES_K(menueid, menue, anzeigename, typ, ebene, untermenuevon, 
				  angelegtvon, angelegtam, ofdb, system, DSID)
				  values(841904, 'AdminTabellenEig', 'Tabellen-Spalten', 'ANSICHT', 2, 'AdminOberflaeche', '#migration#',
                  '24.02.2012', 'X', -1, (select max(t.DSID) + 1 from FX_MENUES_K t ) );

insert into FX_MENUES_K(menueid, menue, anzeigename, typ, ebene, untermenuevon, 
				  angelegtvon, angelegtam, ofdb, system, DSID)
				  values(841905, 'AdminMenues', 'Menüs', 'ANSICHT', 2, 'AdminOberflaeche', '#migration#',
                  '25.02.2012', 'X', -1, (select max(t.DSID) + 1 from FX_MENUES_K t ) );

-- add column urlpath to FX_MENUES_K
alter table FX_MENUES_K add URLPATH varchar2(255);
update FX_MENUES_K set URLPATH = 'tabDef', bereich = 'Administrator' where menueid = 841903;
update FX_MENUES_K set URLPATH = 'tabSpeig', bereich = 'Administrator' where menueid = 841904;

update FX_MENUES_K set URLPATH = 'menues', bereich = 'Administrator' where menueid = 841905;

commit;

alter table FX_MENUES_K add BereichsId number(11,0);

-- add constraint to FX_TabDef_K
alter table FX_MENUES_K
add	 CONSTRAINT "FK_FX_MENUES_K_BereichsID" FOREIGN KEY ("BEREICHSID")
	  REFERENCES "KD_RRE_PROD"."BENUTZERBEREICHEDEF" ("BEREICHSID") ENABLE; 

update FX_MENUES_K m
set m.BereichsId = (select distinct b.BereichsID 
                     from BenutzerBereicheDef b
                    where m.Bereich = b.name
                      );
                      
-- add BereichsId to FX_TabDef_K
alter table FX_TabDef_K add BereichsId number(11,0);

-- add constraint to FX_TabDef_K
alter table FX_TabDef_K
add	 CONSTRAINT "FK_FX_TabDef_K_BereichsID" FOREIGN KEY ("BEREICHSID")
	  REFERENCES "KD_RRE_PROD"."BENUTZERBEREICHEDEF" ("BEREICHSID") ENABLE; 

update FX_TabDef_K t
set t.BereichsId = (select distinct b.BereichsID 
                     from BenutzerBereicheDef b
                    where upper(t.Bereich) = upper(b.name)
                      );
                      
-- insert field BereichsId in FX_TabDef_K, FX_MENUES_K
INSERT INTO FX_TabSpEig_K(Tabelle, Reihenfolge, Spalte, Spaltenkopf, DBDatentyp, PS, Eindeutig, EingabeNotwendig, 
  BearbErlaubt, SystemWert, DefaultWert, Minimum, Maximum, TooltipText, AngelegtVon, AngelegtAm, Ofdb, System, 
  TabDefId, DSID)
VALUES('FX_TabDef_K', 25, 'BereichsId', 'Bereichs_ID', 'LONGINTEGER', 0, null, -1, -1, 0, null, null, null, 
  'ID der Tabelle BenutzerBereicheDef', 'MW#migration#', '27.02.12', 'X', -1, 
  (select tt.dsid from FX_TabDef_K tt where tt.tabelle = 'FX_TabDef_K' and tt.ofdb = 'X'), 
  (select max(t.DSID) + 1 from FX_TabSpEig_K t) );
  
-- insert BereichsId for table FX_TabDef_K into FX_AnsichtSpalten_K
INSERT INTO FX_AnsichtSpalten_K(Ansicht, IndexGrid, TabAKey, SpalteAKey, InGridAnzeigen, InGridLaden, Filter,
  bearbeitenIn, IndexBearb, BearbHinzufZugelassen, BearbZugelassen, AnsichtSuchen, 
  SuchwertAusTabAKey, SuchwertAusSpalteAKey, VerdeckenDurchTabAKey, VerdeckenDurchSpalteAKey, 
  AngelegtVon, AngelegtAm, Ofdb, System, DSID, TabASpalteA)
VALUES('FX_TabDef_K', 25, 'FX_TabDef_K', 'BereichsId', null, -1, -1, 'dcl', null, -1, null, 'BenutzerBereicheDef', 
  'BenutzerBereicheDef', 'BereichsId', 'BenutzerBereicheDef', 'Name', 'MW#migration#', '29.11.2011', 
  'X', -1, (select max(a.DSID)+1 from FX_AnsichtSpalten_K a), 'FX_AnsichtSpalten_K:BereichsId' );    
  
-- neue Spalte BereichsId in FX_MENUES_K
INSERT INTO FX_TabSpEig_K(Tabelle, Reihenfolge, Spalte, Spaltenkopf, DBDatentyp, PS, Eindeutig, EingabeNotwendig, 
  BearbErlaubt, SystemWert, DefaultWert, Minimum, Maximum, TooltipText, AngelegtVon, AngelegtAm, Ofdb, System, 
  TabDefId, DSID)
VALUES('FX_Menues_K', 22, 'BereichsId', 'Bereichs_ID', 'LONGINTEGER', 0, null, -1, 
  -1, 0, null, null, null, 'ID der Tabelle BenutzerBereicheDef', 'MW#migration#', '28.02.12', 'X', -1, 
  (select tt.dsid from FX_TabDef_K tt where tt.tabelle = 'FX_Menues_K' and tt.ofdb = 'X'), 
  (select max(t.DSID) + 1 from FX_TabSpEig_K t) );
  
-- insert BereichsId for table FX_TabDef_K into FX_AnsichtSpalten_K
INSERT INTO FX_AnsichtSpalten_K(Ansicht, IndexGrid, TabAKey, SpalteAKey, InGridAnzeigen, InGridLaden, Filter,
  bearbeitenIn, IndexBearb, BearbHinzufZugelassen, BearbZugelassen, AnsichtSuchen, 
  SuchwertAusTabAKey, SuchwertAusSpalteAKey, VerdeckenDurchTabAKey, VerdeckenDurchSpalteAKey, 
  AngelegtVon, AngelegtAm, Ofdb, System, DSID, TabASpalteA)
VALUES('FX_Menues_K', 22, 'FX_Menues_K', 'BereichsId', -1, -1, -1, 
  'dcl', null, -1, null, 'BenutzerBereicheDef', 
  'BenutzerBereicheDef', 'BereichsId', 'BenutzerBereicheDef', 'Name', 
  'MW#migration#', '29.11.2011', 'X', -1, (select max(a.DSID)+1 from FX_AnsichtSpalten_K a), 
  'FX_AnsichtSpalten_K:BereichsId' ); 
  
INSERT INTO FX_AnsichtSpalten_K(Ansicht, IndexGrid, TabAKey, SpalteAKey, InGridAnzeigen, InGridLaden, Filter,
  bearbeitenIn, IndexBearb, BearbHinzufZugelassen, BearbZugelassen, AnsichtSuchen, 
  SuchwertAusTabAKey, SuchwertAusSpalteAKey, VerdeckenDurchTabAKey, VerdeckenDurchSpalteAKey, 
  AngelegtVon, AngelegtAm, Ofdb, System, DSID, TabASpalteA)
VALUES('FX_Menues_K', 23, 'FX_Menues_K', 'DSID', -1, -1, -1, 
  'dcl', null, null, null, null, 
  null, null, null, null, 
  'MW#migration#', '29.11.2011', 'X', -1, (select max(a.DSID)+1 from FX_AnsichtSpalten_K a), 
  'FX_AnsichtSpalten_K:DSID' ); 
  
  -- urlPath for FX_Menues_K in TabSpeigs
  INSERT INTO FX_TabSpEig_K(Tabelle, Reihenfolge, Spalte, Spaltenkopf, DBDatentyp, PS, Eindeutig, EingabeNotwendig, 
  BearbErlaubt, SystemWert, DefaultWert, Minimum, Maximum, TooltipText, AngelegtVon, AngelegtAm, Ofdb, System, 
  TabDefId, DSID)
VALUES('FX_Menues_K', 23, 'urlPath', 'UrlPath', 'STRING', 0, null, -1, 
  -1, 0, null, null, null, 'Url-Path der Tabelle FX_Menues_K', 'MW#migration#', '02.03.12', 'X', -1, 
  (select tt.dsid from FX_TabDef_K tt where tt.tabelle = 'FX_Menues_K' and tt.ofdb = 'X'), 
  (select max(t.DSID) + 1 from FX_TabSpEig_K t) );
  
INSERT INTO FX_AnsichtSpalten_K(Ansicht, IndexGrid, TabAKey, SpalteAKey, InGridAnzeigen, InGridLaden, Filter,
  bearbeitenIn, IndexBearb, BearbHinzufZugelassen, BearbZugelassen, AnsichtSuchen, 
  SuchwertAusTabAKey, SuchwertAusSpalteAKey, VerdeckenDurchTabAKey, VerdeckenDurchSpalteAKey, 
  AngelegtVon, AngelegtAm, Ofdb, System, DSID, TabASpalteA)
VALUES('FX_Menues_K', 24, 'FX_Menues_K', 'urlPath', -1, -1, -1, 
  'dcl', null, null, null, null, 
  null, null, null, null, 
  'MW#migration#', '02.03.12', 'X', -1, (select max(a.DSID)+1 from FX_AnsichtSpalten_K a), 
  'FX_AnsichtSpalten_K:urlPath' ); 
  
commit;  
  
-- add Spalte aktiv to FX_Menues_K
alter table FX_Menues_K add aktiv number(1);

update FX_Menues_K set aktiv = -1;

INSERT INTO FX_TabSpEig_K(Tabelle, Reihenfolge, Spalte, Spaltenkopf, DBDatentyp, PS, Eindeutig, EingabeNotwendig, 
  BearbErlaubt, SystemWert, DefaultWert, Minimum, Maximum, TooltipText, AngelegtVon, AngelegtAm, Ofdb, System, 
  TabDefId, DSID)
VALUES('FX_Menues_K', 24, 'aktiv', 'aktiv', 'BOOLEAN', 0, null, -1, 
  -1, 0, -1, null, null, 'false, wenn Menue-Eintrag nicht berücksichtigt werden soll', 'MW#migration#', '04.03.12', 'X', -1, 
  (select tt.dsid from FX_TabDef_K tt where tt.tabelle = 'FX_Menues_K' and tt.ofdb = 'X'), 
  (select max(t.DSID) + 1 from FX_TabSpEig_K t) );
  
INSERT INTO FX_AnsichtSpalten_K(Ansicht, IndexGrid, TabAKey, SpalteAKey, InGridAnzeigen, InGridLaden, Filter,
  bearbeitenIn, IndexBearb, BearbHinzufZugelassen, BearbZugelassen, AnsichtSuchen, 
  SuchwertAusTabAKey, SuchwertAusSpalteAKey, VerdeckenDurchTabAKey, VerdeckenDurchSpalteAKey, 
  AngelegtVon, AngelegtAm, Ofdb, System, DSID, TabASpalteA)
VALUES('FX_Menues_K', 25, 'FX_Menues_K', 'aktiv', -1, -1, -1, 
  'dcl', null, -1, -1, null, 
  null, null, null, null, 
  'MW#migration#', '04.03.12', 'X', 0, (select max(a.DSID)+1 from FX_AnsichtSpalten_K a), 
  'FX_AnsichtSpalten_K:aktiv' ); 

update FX_AnsichtSpalten_K set bearbzugelassen = -1 where ansicht = 'FX_Menues_K' and spalteakey = 'BereichsId';

update FX_TabSpeig_K set EingabeNotwendig = 0 where Tabelle = 'FX_Menues_K' and spalte = 'BereichsId';

-- update FX_AnsichtDef_K
-- add column urlpath to FX_MENUES_K
alter table FX_ANSICHTDEF_K add URLPATH varchar2(255);

update FX_ANSICHTDEF_K set URLPATH = 'tabDef', bereich = 'Administrator' where ansicht = 'FX_TabDef_K' and ofdb = 'X';
update FX_ANSICHTDEF_K set URLPATH = 'tabSpeig', bereich = 'Administrator' where ansicht = 'FX_TabSpEig_K' and ofdb = 'X';
update FX_ANSICHTDEF_K set URLPATH = 'menues', bereich = 'Administrator' where ansicht = 'FX_Menues_K' and ofdb = 'X';
  
commit;  
                      
  -- urlPath for FX_AnsichtDef_K in TabSpeigs
  INSERT INTO FX_TabSpEig_K(Tabelle, Reihenfolge, Spalte, Spaltenkopf, DBDatentyp, PS, Eindeutig, EingabeNotwendig, 
  BearbErlaubt, SystemWert, DefaultWert, Minimum, Maximum, TooltipText, AngelegtVon, AngelegtAm, Ofdb, System, 
  TabDefId, DSID)
VALUES('FX_AnsichtDef_K', 33, 'urlPath', 'UrlPath', 'STRING', 0, null, -1, 
  -1, 0, null, null, null, 'Url-Path der Tabelle FX_AnsichtDef_K', 'MW#migration#', '08.03.12', 'X', -1, 
  (select tt.dsid from FX_TabDef_K tt where tt.tabelle = 'FX_AnsichtDef_K' and tt.ofdb = 'X'), 
  (select max(t.DSID) + 1 from FX_TabSpEig_K t) );                      
                      
INSERT INTO FX_AnsichtSpalten_K(Ansicht, IndexGrid, TabAKey, SpalteAKey, InGridAnzeigen, InGridLaden, Filter,
  bearbeitenIn, IndexBearb, BearbHinzufZugelassen, BearbZugelassen, AnsichtSuchen, 
  SuchwertAusTabAKey, SuchwertAusSpalteAKey, VerdeckenDurchTabAKey, VerdeckenDurchSpalteAKey, 
  AngelegtVon, AngelegtAm, Ofdb, System, DSID, TabASpalteA)
VALUES('FX_AnsichtDef_K', 33, 'FX_AnsichtDef_K', 'urlPath', -1, -1, -1, 
  'dcl', null, -1, -1, null, 
  null, null, null, null, 
  'MW#migration#', '08.03.12', 'X', -1, (select max(a.DSID)+1 from FX_AnsichtSpalten_K a), 
  'FX_AnsichtDef_K:urlPath' ); 
  
-- update spalte ansicht in FX_Menues_K
update FX_Menues_K set ansicht = 'FX_TabDef_K' where menue = 'AdminTabellen' and anzeigename = 'Tabellen';
update FX_Menues_K set ansicht = 'FX_TabSpEig_K' where menue = 'AdminTabellenEig';
update FX_Menues_K set ansicht = 'FX_Menues_K' where menue = 'AdminMenues';

-- urlPath for FX_AnsichtDef_K in TabSpeigs
INSERT INTO FX_TabSpEig_K(Tabelle, Reihenfolge, Spalte, Spaltenkopf, DBDatentyp, PS, Eindeutig, EingabeNotwendig, 
 BearbErlaubt, SystemWert, DefaultWert, Minimum, Maximum, TooltipText, AngelegtVon, AngelegtAm, Ofdb, System, 
 TabDefId, DSID)
VALUES('FX_TabDef_K', (select max(reihenfolge)+1 from FX_TabSpEig_K where tabelle = 'FX_TabDef_K' and ofdb = 'X' ), 
 'fullClassName', 'fullClassName', 'STRING', 0, -1, 0, 
 -1, 0, null, null, null, 'vollqualifizierter Java-Klassenname der Domänen-Klasse', 
 'MW#migration#', '11.03.12', 'X', -1, 
 (select tt.dsid from FX_TabDef_K tt where tt.tabelle = 'FX_TabDef_K' and tt.ofdb = 'X'), 
 (select max(t.DSID) + 1 from FX_TabSpEig_K t) ); 
 
 INSERT INTO FX_AnsichtSpalten_K(Ansicht, IndexGrid, TabAKey, SpalteAKey, InGridAnzeigen, InGridLaden, Filter,
  bearbeitenIn, IndexBearb, BearbHinzufZugelassen, BearbZugelassen, AnsichtSuchen, 
  SuchwertAusTabAKey, SuchwertAusSpalteAKey, VerdeckenDurchTabAKey, VerdeckenDurchSpalteAKey, 
  AngelegtVon, AngelegtAm, Ofdb, System, DSID, TabASpalteA)
VALUES('FX_TabDef_K', (select max(a.indexgrid) +1 from FX_AnsichtSpalten_K a where a.ansicht = 'FX_TabDef_K' and a.ofdb = 'X' ), 
  'FX_TabDef_K', 'fullClassName', -1, -1, -1, 
  'dcl', null, -1, -1, null, 
  null, null, null, null, 
  'MW#migration#', '11.03.12', 'X', -1, (select max(aa.DSID)+1 from FX_AnsichtSpalten_K aa), 
  'FX_AnsichtDef_K:fullClassName' );
  
alter table FX_TabDef_K add fullclassname varchar(255);
  
update FX_TabDef_K set fullclassname = 'de.mw.mwdata.core.ofdb.domain.TabSpeig' where tabelle  = 'FX_TabSpEig_K';  
  
-- add AnsichtDefId to FX_Menues_K
alter table FX_Menues_K add AnsichtDefId number(11,0);

-- add constraint to FX_Menues_K
alter table FX_Menues_K
add	 CONSTRAINT "FK_FX_Menues_K_AnsichtDefID" FOREIGN KEY ("ANSICHTDEFID")
	  REFERENCES "KD_RRE_PROD"."FX_ANSICHTDEF_K" ("DSID") ENABLE; 

update FX_Menues_K m 
set m.ansichtdefid = (select distinct a.dsid from FX_AnsichtDef_K a 
                      where upper(m.ansicht) = upper(a.ansicht)
                      and m.typ = 'ANSICHT')
where exists (select 1 from FX_AnsichtDef_K aa 
              where upper(m.ansicht) = upper(aa.ansicht)
              and m.typ = 'ANSICHT')                      
;
                      
INSERT INTO FX_TabSpEig_K(Tabelle, Reihenfolge, Spalte, Spaltenkopf, DBDatentyp, PS, Eindeutig, EingabeNotwendig, 
 BearbErlaubt, SystemWert, DefaultWert, Minimum, Maximum, TooltipText, AngelegtVon, AngelegtAm, Ofdb, System, 
 TabDefId, DSID)
VALUES('FX_Menues_K', (select max(reihenfolge)+1 from FX_TabSpEig_K where tabelle = 'FX_Menues_K' and ofdb = 'X' ), 
 'AnsichtDefId', 'AnsichtDefId', 'LONGINTEGER', 0, -1, -1, 
 -1, 0, null, null, null, 'Id der Ansicht zu diesem Menue-Eintrag', 
 'MW#migration#', '14.03.12', 'X', -1, 
 (select tt.dsid from FX_TabDef_K tt where tt.tabelle = 'FX_Menues_K' and tt.ofdb = 'X'), 
 (select max(t.DSID) + 1 from FX_TabSpEig_K t) ); 
 
  INSERT INTO FX_AnsichtSpalten_K(Ansicht, IndexGrid, TabAKey, SpalteAKey, InGridAnzeigen, InGridLaden, Filter,
  bearbeitenIn, IndexBearb, BearbHinzufZugelassen, BearbZugelassen, AnsichtSuchen, 
  SuchwertAusTabAKey, SuchwertAusSpalteAKey, VerdeckenDurchTabAKey, VerdeckenDurchSpalteAKey, 
  AngelegtVon, AngelegtAm, Ofdb, System, DSID, TabASpalteA)
VALUES('FX_Menues_K', (select max(a.indexgrid) +1 from FX_AnsichtSpalten_K a where a.ansicht = 'FX_Menues_K' and a.ofdb = 'X' ), 
  'FX_Menues_K', 'AnsichtDefId', -1, -1, -1, 
  'dcl', null, -1, -1, 'FX_AnsichtDef_K', 
  'FX_AnsichtDef_K', 'DSID', 'FX_AnsichtDef_K', 'Ansicht', 
  'MW#migration#', '14.03.12', 'X', -1, (select max(aa.DSID)+1 from FX_AnsichtSpalten_K aa), 
  'FX_Menues_K:AnsichtDefId' );
  
-- korrekturen
update FX_AnsichtSpalten_K set tabaspaltea = 'FX_Menues_K:BereichsId' where ansicht = 'FX_Menues_K' and ofdb = 'X' and spalteakey = 'BereichsId';

update FX_AnsichtSpalten_K set tabaspaltea = 'FX_Menues_K:DSID' where ansicht = 'FX_Menues_K' and ofdb = 'X' and spalteakey = 'DSID';

update FX_AnsichtSpalten_K set tabaspaltea = 'FX_Menues_K:urlPath' where ansicht = 'FX_Menues_K' and ofdb = 'X' and spalteakey = 'urlPath';

update FX_AnsichtSpalten_K set tabaspaltea = 'FX_Menues_K:aktiv' where ansicht = 'FX_Menues_K' and ofdb = 'X' and spalteakey = 'aktiv';

commit;

-- add HauptMenueId to FX_Menues_K
alter table FX_Menues_K add HauptMenueId number(11,0);

-- add constraint to FX_Menues_K
alter table FX_Menues_K
add	 CONSTRAINT "FK_FX_Menues_K_HauptMenueID" FOREIGN KEY ("HAUPTMENUEID")
	  REFERENCES "KD_RRE_PROD"."FX_MENUES_K" ("DSID") ENABLE; 
    
update FX_Menues_K m1
set m1.HauptmenueId = (select distinct m0.dsid
  from FX_Menues_K m0 
  where upper(m1.untermenuevon) = upper(m0.menue)
  and m0.ebene = 0
  and m0.typ = 'KNOTEN'
  )
where exists( select 1 from FX_Menues_K m 
    where upper(m1.untermenuevon) = upper(m.menue)
      and m.ebene = 0
      and m1.ebene = 1
      and m.typ = 'KNOTEN')
and m1.untermenuevon is not null
and m1.ebene = 1
--and m1.untermenuevon like 'Ab%'
;

update FX_Menues_K m1
set m1.HauptmenueId = (select distinct m0.dsid
  from FX_Menues_K m0 
  where upper(m1.untermenuevon) = upper(m0.menue)
  and m0.ebene = 1
  and m0.typ = 'KNOTEN'
  )
where exists( select 1 from FX_Menues_K m 
    where upper(m1.untermenuevon) = upper(m.menue)
      and m.ebene = 1
      and m1.ebene = 2
      and m.typ = 'KNOTEN')
and m1.untermenuevon is not null
and m1.ebene = 2
--and m1.untermenuevon like 'Ab%'
;

update FX_Menues_K m1
set m1.HauptmenueId = (select distinct m0.dsid
  from FX_Menues_K m0 
  where upper(m1.untermenuevon) = upper(m0.menue)
  and m0.ebene = 2
  and m0.typ = 'KNOTEN'
  )
where exists( select 1 from FX_Menues_K m 
    where upper(m1.untermenuevon) = upper(m.menue)
      and m.ebene = 2
      and m1.ebene = 3
      and m.typ = 'KNOTEN')
and m1.untermenuevon is not null
and m1.ebene = 3
--and m1.untermenuevon like 'Ab%'
;

INSERT INTO FX_TabSpEig_K(Tabelle, Reihenfolge, Spalte, Spaltenkopf, DBDatentyp, PS, Eindeutig, EingabeNotwendig, 
 BearbErlaubt, SystemWert, DefaultWert, Minimum, Maximum, TooltipText, AngelegtVon, AngelegtAm, Ofdb, System, 
 TabDefId, DSID)
VALUES('FX_Menues_K', (select max(reihenfolge)+1 from FX_TabSpEig_K where tabelle = 'FX_Menues_K' and ofdb = 'X' ), 
 'HauptMenueId', 'HauptMenueId', 'LONGINTEGER', 0, 0, 0, 
 -1, 0, null, null, null, 'Id des Hauptmenues zu diesem Menue-Eintrag', 
 'MW#migration#', '16.03.12', 'X', -1, 
 (select tt.dsid from FX_TabDef_K tt where tt.tabelle = 'FX_Menues_K' and tt.ofdb = 'X'), 
 (select max(t.DSID) + 1 from FX_TabSpEig_K t) ); 
 
  INSERT INTO FX_AnsichtSpalten_K(Ansicht, IndexGrid, TabAKey, SpalteAKey, InGridAnzeigen, InGridLaden, Filter,
  bearbeitenIn, IndexBearb, BearbHinzufZugelassen, BearbZugelassen, AnsichtSuchen, 
  SuchwertAusTabAKey, SuchwertAusSpalteAKey, VerdeckenDurchTabAKey, VerdeckenDurchSpalteAKey, 
  AngelegtVon, AngelegtAm, Ofdb, System, DSID, TabASpalteA)
VALUES('FX_Menues_K', (select max(a.indexgrid) +1 from FX_AnsichtSpalten_K a where a.ansicht = 'FX_Menues_K' and a.ofdb = 'X' ), 
  'FX_Menues_K', 'HauptMenueId', -1, -1, -1, 
  'dcl', null, -1, -1, 'FX_Menues_K', 
  'FX_Menues_K', 'DSID', 'FX_Menues_K', 'Menue', 
  'MW#migration#', '16.03.12', 'X', -1, (select max(aa.DSID)+1 from FX_AnsichtSpalten_K aa), 
  'FX_Menues_K:HauptMenueId' );
  
update FX_AnsichtDef_K set ansicht = 'BenutzerBereicheDef' where ansicht = 'BenutzerBereichedef' and ofdb = 'X';

update FX_TabDef_K set fullclassname = 'de.mw.mwdata.core.ofdb.domain.Menue' where tabelle = 'FX_Menues_K' and ofdb = 'X';

update FX_TabDef_K set fullClassName = 'de.mw.mwdata.core.ofdb.domain.TabDef' where tabelle = 'FX_TabDef_K' and ofdb = 'X';

update FX_TabDef_K set fullClassName = 'de.mw.mwdata.core.domain.BenutzerBereich' where tabelle = 'BenutzerBereicheDef' and ofdb = 'X';

update FX_TabDef_K set fullClassName = 'de.mw.mwdata.core.ofdb.domain.AnsichtDef' where tabelle = 'FX_AnsichtDef_K' and ofdb = 'X';

update FX_TabDef_K set fullClassName = 'de.mw.mwdata.core.ofdb.domain.AnsichtSpalten' where tabelle = 'FX_AnsichtSpalten_K' and ofdb = 'X';

update FX_TabSpeig_K set eingabenotwendig = 0 where tabelle = 'FX_Menues_K' and spalte = 'AnsichtDefId';

-- UPDATE BENUTZER
update FX_TabDef_K set tabelle = 'BenutzerDef' where tabelle = 'Benutzerdef';

alter table BENUTZERDEF add Feld1 varchar(255) null;
update BENUTZERDEF set Feld1 = Feld01;
alter table BENUTZERDEF drop column Feld01;

alter table BENUTZERDEF add Feld2 varchar(255) null;
update BENUTZERDEF set Feld2 = Feld02;
alter table BENUTZERDEF drop column Feld02;

alter table BENUTZERDEF add Feld3 varchar(255) null;
update BENUTZERDEF set Feld3 = Feld03;
alter table BENUTZERDEF drop column Feld03;

alter table BENUTZERDEF add Feld4 varchar(255) null;
update BENUTZERDEF set Feld4 = Feld04;
alter table BENUTZERDEF drop column Feld04;

alter table BENUTZERDEF add Feld5 varchar(255) null;
update BENUTZERDEF set Feld5 = Feld05;
alter table BENUTZERDEF drop column Feld05;

alter table BENUTZERDEF add Feld6 varchar(255) null;
update BENUTZERDEF set Feld6 = Feld06;
alter table BENUTZERDEF drop column Feld06;

alter table BENUTZERDEF add ImportId number(11,0) null;

-- repair BENUTZERRECHTE
alter table BENUTZERRECHTE add Feld1 varchar(255) null;
alter table BENUTZERRECHTE add Feld2 varchar(255) null;
alter table BENUTZERRECHTE add Feld3 varchar(255) null;

update FX_TabDef_K set fullclassname = 'de.mw.mwdata.core.domain.Benutzer' where tabelle = 'BenutzerDef';

update FX_TabDef_K set fullclassname = 'de.mw.mwdata.core.domain.BenutzerRecht', tabelle = 'BenutzerRechte'
where tabelle = 'Benutzerrechte';

-- neue Menue-Eintraege für AnsichtDef, AnsichtSpalten
insert into FX_MENUES_K(menueid, menue, anzeigename, typ, ebene, untermenuevon, 
				  angelegtvon, angelegtam, ofdb, system, DSID, Ansicht, AnsichtDefID, Bereich, BEREICHSID, urlpath, aktiv, hauptmenueid)
				  values(841906, 'AdminAnsichten', 'Ansichten', 'ANSICHT', 2, 'AdminOberflaeche', '#migration#',
                  '13.06.2012', 'X', -1, (select max(t.DSID) + 1 from FX_MENUES_K t ),
                 'FX_AnsichtDef_K', (Select dsid from FX_AnsichtDef_K where ansicht = 'FX_AnsichtDef_K'),
                 'Administrator', (select bereichsid from BENUTZERBEREICHEDEF where name = 'Administrator'), 
                 'ansichtDef', -1, (select dsid from FX_Menues_K where menue = 'AdminOberflaeche') );
                 
insert into FX_MENUES_K(menueid, menue, anzeigename, typ, ebene, untermenuevon, 
				  angelegtvon, angelegtam, ofdb, system, DSID, Ansicht, AnsichtDefID, Bereich, BEREICHSID, urlpath, aktiv, hauptmenueid)
				  values(841907, 'AdminAnsichtSpalten', 'Ansichtsspalten', 'ANSICHT', 2, 'AdminOberflaeche', '#migration#',
                  '13.06.2012', 'X', -1, (select max(t.DSID) + 1 from FX_MENUES_K t ),
                 'FX_AnsichtSpalten_K', (Select dsid from FX_AnsichtDef_K where ansicht = 'FX_AnsichtSpalten_K'),
                 'Administrator', (select bereichsid from BENUTZERBEREICHEDEF where name = 'Administrator'), 
                 'ansichtSpalten', -1, (select dsid from FX_Menues_K where menue = 'AdminOberflaeche') );  
                 
update FX_AnsichtDef_K set urlpath = 'ansichtDef', bereich = 'Administrator' where ansicht = 'FX_AnsichtDef_K';
update FX_AnsichtDef_K set urlpath = 'ansichtSpalten', bereich = 'Administrator' where ansicht = 'FX_AnsichtSpalten_K';                 

-- add missing bereiche Bankkonten, Entgelt, Hauptbuchhaltung
INSERT INTO BenutzerBereicheDef (abrechnen, bearbeiten, berechnen, exportieren, hinzufuegen, importieren, lesen,
  loeschen, stornieren, vergebbar, Beschreibung, name, bereichsid, unterschrift1, unterschrift2, unterschrift3, 
  alleds, verschluesseln, nameGB, adminautozuweis, angelegtvon, angelegtam, transaktionsid, ofdb)   
VALUES (0, 0, 0, 0, 0, 0, -1, 0, 0, 0, 'Entgelt', 'Entgelt', 
  (select max(b2.bereichsid) + 1 from BenutzerBereicheDef b2), 
  0, 0, 0, -1, 0, 'ENTGELT', -1, 0, '24.05.2017', 0, 'X');
  
  INSERT INTO BenutzerBereicheDef (abrechnen, bearbeiten, berechnen, exportieren, hinzufuegen, importieren, lesen,
  loeschen, stornieren, vergebbar, Beschreibung, name, bereichsid, unterschrift1, unterschrift2, unterschrift3, 
  alleds, verschluesseln, nameGB, adminautozuweis, angelegtvon, angelegtam, transaktionsid, ofdb)   
VALUES (0, 0, 0, 0, 0, 0, -1, 0, 0, 0, 'Bankkonten', 'Bankkonten', 
  (select max(b2.bereichsid) + 1 from BenutzerBereicheDef b2), 
  0, 0, 0, -1, 0, 'BANKKONTEN', -1, 0, '24.05.2017', 0, 'X');

 INSERT INTO BenutzerBereicheDef (abrechnen, bearbeiten, berechnen, exportieren, hinzufuegen, importieren, lesen,
  loeschen, stornieren, vergebbar, Beschreibung, name, bereichsid, unterschrift1, unterschrift2, unterschrift3, 
  alleds, verschluesseln, nameGB, adminautozuweis, angelegtvon, angelegtam, transaktionsid, ofdb)   
VALUES (0, 0, 0, 0, 0, 0, -1, 0, 0, 0, 'Hauptbuchhaltung', 'Hauptbuchhaltung', 
  (select max(b2.bereichsid) + 1 from BenutzerBereicheDef b2), 
  0, 0, 0, -1, 0, 'HAUPTBUCHHALTUNG', -1, 0, '24.05.2017', 0, 'X');

-- missing bereichsids in FX_TAbDef_K nachrüsten
update FX_TabDef_K t 
set t.BereichsID = ( select b.bereichsid from BenutzerBereicheDef b
                    where upper(b.Name ) = upper(t.Bereich) )
where t.Bereichsid is null;  
  
alter table FX_TabDef_K modify bereichsid number(11,0) not null;

-- add BereichsId not nullto FX_AnsichtDef_K and set values
alter table FX_AnsichtDef_K add BereichsId number(11,0);

alter table FX_AnsichtDef_K
add	 CONSTRAINT "FK_FX_AnsichtDef_K_BereichsID" FOREIGN KEY ("BEREICHSID")
	  REFERENCES "KD_RRE_PROD"."BENUTZERBEREICHEDEF" ("BEREICHSID") ENABLE; 

update FX_AnsichtDef_K a
set a.BereichsId = (select distinct b.BereichsID 
                     from BenutzerBereicheDef b
                    where upper(a.Bereich) = upper(b.name)
                      );

-- set all missing Bereich-values to 'Frei'                      
update FX_AnsichtDef_K set bereichsid = (select b.bereichsid from BENUTZERBEREICHEDEF b where name = 'Frei')
where bereichsid is null;

commit;

alter table FX_AnsichtDef_K modify bereichsid number(11,0) not null;

alter table ADANSICHTENDEF add ofdb varchar(255) not null;
alter table ADANSICHTENDEF add system numeric(1) default -1 not null;

-- add refkey from ADANSICHTENDEF to FX_AnsichtDef_K
alter table ADANSICHTENDEF add ANSICHTID number(11,0) not null;

alter table ADANSICHTENDEF
add	 CONSTRAINT "FK_ADANSICHTENDEF_ANSICHTID" FOREIGN KEY ("ANSICHTID")
	  REFERENCES FX_AnsichtDef_K ("DSID") ENABLE; 
    
alter table ADANSICHTENDEF add DefaultAnsicht number(1) not null;    

-- add ofdb to ADANSICHTSPALTEN
alter table ADANSICHTSPALTEN add ofdb varchar(255);
update ADANSICHTSPALTEN set ofdb = 'X';
alter table ADANSICHTSPALTEN modify ofdb varchar(255) not null;

-- add system to ADANSICHTSPALTEN
alter table ADANSICHTSPALTEN add system number(1);
update ADANSICHTSPALTEN set system = -1;
alter table ADANSICHTSPALTEN modify system number(1) default -1 not null;

-- copy views from FX_AnsichteDef_K to ADAnsichtenDef
insert into ADAnsichtenDef(DSID, transaktionsid, angelegtvon, angelegtam, ansicht, importid, ndsxanzeigen, anzahlspaltenfixiert,
bezeichnung, kopievon, sqlscript, sqlscripttyp, benutzerid, ofdb, system, ansichtid, defaultansicht)
select nvl((select max(a.dsid) from adansichtendef a),0) + 1 + rownum, 1, 0, to_date('25.11.2014', 'dd.mm.yyyy'), ansicht, 0, ndsxanzeigen, 0, 
null, null, sqlscript, sqlscripttyp, 0, 'X', -1, dsid, -1
from FX_AnsichtDef_K 
where 1=1 
and urlpath is not null
;

-- set foreign key from FX_AnsichtSpalten_K to FX_AnsichtDef_K
alter table FX_AnsichtSpalten_K add AnsichtDefId number(11,0);

-- add constraint to FX_Menues_K
alter table FX_AnsichtSpalten_K
add	 CONSTRAINT "FK_FX_AnsSpalt_K_AnsichtDefID" FOREIGN KEY ("ANSICHTDEFID")
	  REFERENCES "KD_RRE_PROD"."FX_ANSICHTDEF_K" ("DSID") ENABLE; 

-- update AnsichtDefId von FX_AnsichtDef_K auf FX_AnsichtSpalten_K:AnsichtDefId
update FX_AnsichtSpalten_K ass
set ass.AnsichtDefId = 
  (select a.dsid from FX_AnsichtDef_K a
   where 1=1
   and a.ansicht = ass.ansicht)
where exists (
  select 1 from FX_AnsichtDef_K ad
  where 1=1
  and ad.ansicht = ass.ansicht
)   
;

-- fix invalid datasets in FX_AnsichtSpalten_K with wrong ansicht not mapping to FX_AnsichtDef_K
update FX_AnsichtSpalten_K ass
set ass.ansicht = 
  (select a.ansicht from FX_AnsichtDef_K a
   where 1=1
   and upper(a.ansicht) = upper(ass.ansicht))
where exists (
  select 1 from FX_AnsichtDef_K ad
  where 1=1
  and upper(ad.ansicht) = upper(ass.ansicht)
)  
and ass.ANSICHTDEFID is null
;

-- update ansichtDefid of fixed invalid datasets from FX_AnsichtSpalten_K
update FX_AnsichtSpalten_K ass
set ass.AnsichtDefId = 
  (select a.dsid from FX_AnsichtDef_K a
   where 1=1
   and a.ansicht = ass.ansicht)
where exists (
  select 1 from FX_AnsichtDef_K ad
  where 1=1
  and ad.ansicht = ass.ansicht
)   
and ass.ansichtdefid is null
;

alter table FX_AnsichtSpalten_K modify  AnsichtDefId number(11,0) not null;

-- reset old column ansicht nullable - only for further informations
alter table FX_AnsichtSpalten_K modify Ansicht varchar2(255) null;

-- migrate ADANSICHTSPALTEN with copies of all defined spalten from FX_ANSICHTSPALTEN_K
alter table ADANSICHTSPALTEN add ANSICHTSPALTENDEFID number(11,0);

delete from ADANSICHTSPALTEN;

-- remove FK to SysKdAnsichtenDef
alter table ADANSICHTSPALTEN DROP constraint FK_ADANSICHTSPALTEN_ANSID;

commit;

-- initialize ADANSICHTSPALTEN with default columns from FX_AnsichtSpalten_K
insert into ADANSICHTSPALTEN(DSID, ANSICHTID, TRANSAKTIONSID, FILTER, INGRIDANZEIGEN, ANGELEGTVON,
				ANGELEGTAM, IMPORTID,BEARBZUGELASSEN, BEARBHINZUFZUGELASSEN,
				SPALTEASKEY, OFDB, SYSTEM, ANSICHTSPALTENDEFID, indsbanzeigen)
				select nvl((select max(a.dsid) from ADANSICHTSPALTEN a),0)  + rownum,  ada.dsid, 1, 0, -1, 1,
				to_date('19.12.2014', 'dd.mm.yyyy'), 0, -1, -1, asp.SPALTEAKEY, 'X', 0, asp.DSID, 0
				from ADANSICHTENDEF ada, FX_AnsichtDef_K ad, FX_AnsichtSpalten_K asp
				where 1=1
				and ada.ansichtid = ad.DSID
				and ad.DSID = asp.ANSICHTDEFID;
        
        
-- change ADANSICHTENDEF.angelegtVon number auf ADANSICHTENDEF.angelegtVon varchar
alter table ADANSICHTENDEF add  angelegtVonTemp varchar2(255);
update ADANSICHTENDEF set angelegtVonTemp = angelegtVon;
alter table ADANSICHTENDEF drop column angelegtVon;
alter table ADANSICHTENDEF add angelegtVon varchar2(255);
update ADANSICHTENDEF set angelegtVon = angelegtVonTemp;
alter table ADANSICHTENDEF modify angelegtVon varchar2(255) not null;

-- change ADANSICHTSPALTEN.angelegtVon number auf ADANSICHTSPALTEN.angelegtVon varchar
alter table ADANSICHTSPALTEN add  angelegtVonTemp varchar2(255);
update ADANSICHTSPALTEN set angelegtVonTemp = angelegtVon;
alter table ADANSICHTSPALTEN drop column angelegtVon;
alter table ADANSICHTSPALTEN add angelegtVon varchar2(255);
update ADANSICHTSPALTEN set angelegtVon = angelegtVonTemp;
alter table ADANSICHTSPALTEN modify angelegtVon varchar2(255) not null;
alter table ADANSICHTSPALTEN drop column angelegtVonTemp;

-- change BenutzerBereicheDef.angelegtVon number auf BenutzerBereicheDef.angelegtVon varchar(2)
alter table BenutzerBereicheDef add  angelegtVonTemp varchar2(255);
update BenutzerBereicheDef set angelegtVonTemp = angelegtVon;
alter table BenutzerBereicheDef drop column angelegtVon;
alter table BenutzerBereicheDef add angelegtVon varchar2(255);
update BenutzerBereicheDef set angelegtVon = angelegtVonTemp;
alter table BenutzerBereicheDef modify angelegtVon varchar2(255) not null;
alter table BenutzerBereicheDef drop column angelegtVonTemp;

-- change BenutzerDef.angelegtVon number auf BenutzerDef.angelegtVon varchar
alter table BenutzerDef add  angelegtVonTemp varchar2(255);
update BenutzerDef set angelegtVonTemp = angelegtVon;
alter table BenutzerDef drop column angelegtVon;
alter table BenutzerDef add angelegtVon varchar2(255);
update BenutzerDef set angelegtVon = angelegtVonTemp;
alter table BenutzerDef drop column angelegtVonTEmp;
alter table BenutzerDef modify angelegtVon varchar2(255) not null;

-- change BenutzerRechte.angelegtVon auf BenutzerRechte.angelegtVon
alter table BenutzerRechte add  angelegtVonTemp varchar2(255);
update BenutzerRechte set angelegtVonTemp = angelegtVon;
alter table BenutzerRechte drop column angelegtVon;
alter table BenutzerRechte add angelegtVon varchar2(255);
update BenutzerRechte set angelegtVon = angelegtVonTemp;
alter table BenutzerRechte modify angelegtVon varchar2(255) not null;
alter table BenutzerRechte drop column angelegtVonTemp;

-- update FX_AnsichtOrderBy_k.ansicht auf den Wert von FX_AnsichtDef_K.ansicht
update FX_AnsichtOrderBy_K o
set o.ansicht = (
  select ad.ansicht
  from FX_AnsichtDef_K ad
  where upper(ad.ansicht) = upper(o.ansicht)
)
where exists(
  select 1 from FX_AnsichtDef_K adWhere
  where upper(adWhere.ansicht) = upper(o.ansicht)
)
;

-- add AnsichtDefId to FX_Menues_K
alter table FX_AnsichtOrderBy_K add AnsichtDefId number(11,0);

update FX_AnsichtOrderBy_K o
set o.ANSICHTDEFID = (
  select ad.DSID
  from FX_AnsichtDef_K ad
  where ad.ansicht = o.ansicht
)
;

-- fix, synchronize ansichtnames in FX_AnsichtTab_K to FX_AnsichtDef_K.ANSICHT
update FX_AnsichtTab_K att
set att.ansicht = (
  select ad.ansicht
  from FX_AnsichtDef_K ad
  where upper(ad.ansicht) = upper(att.ansicht)
)
where exists(
  select 1 from FX_AnsichtDef_K adWhere
  where upper(adWhere.ansicht) = upper(att.ansicht)
)
;

alter table FX_AnsichtTab_K add AnsichtDefId number(11,0);

-- initialize new ansichtdefid of FX_AnsichtTab_K
update FX_AnsichtTab_K att
set att.ANSICHTDEFID = (
  select ad.DSID
  from FX_AnsichtDef_K ad
  where upper(ad.ansicht) = upper(att.ansicht)
)
where exists(
  select 1 from FX_AnsichtDef_K adWhere
  where upper(adWhere.ansicht) = upper(att.ansicht)
)
;

commit;

alter table FX_AnsichtTab_K modify  ansichtdefid number(11,0) not null;

-- add tabdefid to FX_AnsichtTab_K and update
alter table FX_AnsichtTab_K add TABDEFID number(11,0);

update FX_AnsichtTab_K att
set att.tabdefid = (
  select t.dsid
  from FX_TabDef_K t
  where upper(t.tabelle) = upper(att.tabakey)
)
;

update FX_AnsichtTab_K att
set att.tabdefid = (
  select t.dsid
  from FX_TabDef_K t
  where upper(t.tabelle) = upper(att.tabelle)
)
where att.tabdefid is null
;

commit;

alter table FX_AnsichtTab_K modify TABDEFID number(11,0) not null;

-- update appcontextpath
alter table FX_AnsichtDef_K add APPCONTEXTPATH varchar(255);

update FX_AnsichtDef_K set appcontextpath = 'admin' where urlpath is not null;

alter table FX_AnsichtDef_K add REIHENFOLGE number(3,0);

-- change SysSequenz.angelegtVon number auf SysSequenz.angelegtVon varchar
alter table SysSequenz add  angelegtVonTemp varchar2(255);
update SysSequenz set angelegtVonTemp = angelegtVon;
alter table SysSequenz drop column angelegtVon;
alter table SysSequenz add angelegtVon varchar2(255);
update SysSequenz set angelegtVon = angelegtVonTemp;
alter table SysSequenz drop column angelegtVonTEmp;
alter table SysSequenz modify angelegtVon varchar2(255) not null;

-- initialize reihenfolge of BenutzerBereicheDef
update FX_AnsichtDef_K set reihenfolge  = 5, bereich = 'Administrator', urlpath = 'benutzerBereiche', appcontextpath = 'admin' where ansicht = 'BenutzerBereicheDef';

update FX_AnsichtDef_K set reihenfolge  = 10 where ansicht = 'FX_TabDef_K';

update FX_AnsichtDef_K set reihenfolge  = 20 where ansicht = 'FX_TabSpEig_K';

update FX_AnsichtDef_K set reihenfolge  = 30 where ansicht = 'FX_AnsichtDef_K';

update FX_AnsichtDef_K set reihenfolge  = 40 where ansicht = 'FX_AnsichtSpalten_K';

update FX_AnsichtDef_K set reihenfolge  = 50 where ansicht = 'FX_Menues_K';

-- fix all entries for BenutzerBereicheDef
update FX_AnsichtTab_K set tabakey = 'BenutzerBereicheDef' where ansicht  = 'BenutzerBereicheDef' and jointyp = 'x' and tabelle is null;

update FX_AnsichtSpalten_K set tabakey = 'BenutzerBereicheDef' 
where tabakey = 'BenutzerBereichedef' and ansichtdefid = (
  select aa.DSID
  from FX_AnsichtDef_K aa where aa.ANSICHT = 'BenutzerBereicheDef'
);

update FX_AnsichtOrderBy_K set tabakey = 'BenutzerBereicheDef'
where ansichtdefid = (
  select aa.dsid from FX_AnsichtDef_K aa where aa.ANSICHT = 'BenutzerBereicheDef'
);

commit;

-- add BenutzerBereicheDef to FX_TabDef.ansichtTabs
insert into FX_AnsichtTab_K(ansicht, reihenfolge, tabakey, 
jointyp, join1spalteakey, join2tabakey, join2spalteakey,
angelegtvon, angelegtam, ofdb, system, dsid, ansichtdefid, tabdefid)
values('FX_TabDef_K', 2, 'BenutzerBereicheDef', 
'left', 'BereichsID', 'FX_TabDef_K', 'BereichsId', 
'MW#migration#', '03.02.15', 'X', -1, 
(select max(at.DSID)+1 from FX_AnsichtTab_K at ),
(select ad.dsid from FX_AnsichtDef_K ad where ad.ansicht = 'FX_TabDef_K'),
(select td.dsid from FX_TabDef_K td where tabelle = 'BenutzerBereicheDef')
)
;

-- set some FK in FX_AnsichtTab_K
alter table FX_AnsichtTab_K
add	 CONSTRAINT "FK_FX_AnsichtTab_ANSICHTDEFID" FOREIGN KEY ("ANSICHTDEFID")
	  REFERENCES "FX_ANSICHTDEF_K" ("DSID") ENABLE; 

alter table FX_AnsichtTab_K
add	 CONSTRAINT "FK_FX_AnsichtTab_TABDEFID" FOREIGN KEY ("TABDEFID")
	  REFERENCES "FX_TABDEF_K" ("DSID") ENABLE;
    
-- fix nach �nderung case-sensitive read of FX_TabSpEig_K.spalte    
update FX_TabSpEig_K set spalte = 'verschluesseln' 
where tabelle = 'BenutzerBereicheDef' and spalte = 'Verschluesseln';    

update FX_TabSpEig_K set spalte = 'Passphrase' 
where tabelle = 'BenutzerBereicheDef' and spalte = 'PassPhrase';

update FX_TabSpEig_K set spalte = 'zuletztGeaendertAm' 
where tabelle = 'BenutzerBereicheDef' and spalte = 'ZuletztGeaendertAm';

update FX_TabSpEig_K set spalte = 'BereichsId' 
where tabelle = 'BenutzerBereicheDef' and spalte = 'BereichsID';

update FX_AnsichtSpalten_K
set spalteakey = 'BereichsId'
where ansicht = 'BenutzerBereicheDef' and spalteakey = 'BereichsID';

-- add FX_TabDef_K to AnsichtTab for FX_TabSpEig_K
insert into FX_AnsichtTab_K(ansicht, reihenfolge, tabakey, 
jointyp, join1spalteakey, join2tabakey, join2spalteakey,
angelegtvon, angelegtam, ofdb, system, dsid, ansichtdefid, tabdefid)
values('FX_TabSpEig_K', 2, 'FX_TabDef_K', 
'left', 'DSID', 'FX_TabSpEig_K', 'TabDefId', 
'MW#migration#', '03.03.15', 'X', -1, 
(select max(at.DSID)+1 from FX_AnsichtTab_K at ),
(select ad.dsid from FX_AnsichtDef_K ad where ad.ansicht = 'FX_TabSpEig_K'),
(select td.dsid from FX_TabDef_K td where tabelle = 'FX_TabDef_K')
);

-- add Tab BenutzerBereicheDef to FX_AnsichtTab_K = FX_Menues_K
insert into FX_AnsichtTab_K(ansicht, reihenfolge, tabakey, 
jointyp, join1spalteakey, join2tabakey, join2spalteakey,
angelegtvon, angelegtam, ofdb, system, dsid, ansichtdefid, tabdefid)
values('FX_Menues_K', 2, 'BenutzerBereicheDef', 
'left', 'BereichsId', 'FX_Menues_K', 'BereichsId', 
'MW#migration#', '03.03.15', 'X', -1, 
(select max(at.DSID)+1 from FX_AnsichtTab_K at ),
(select ad.dsid from FX_AnsichtDef_K ad where ad.ansicht = 'FX_Menues_K'),
(select td.dsid from FX_TabDef_K td where tabelle = 'BenutzerBereicheDef')
);

-- add entry for FX_AnsichtDef_K to FX_AnsichtTab_K for FX_Menues_K
insert into FX_AnsichtTab_K(ansicht, reihenfolge, tabakey, 
jointyp, join1spalteakey, join2tabakey, join2spalteakey,
angelegtvon, angelegtam, ofdb, system, dsid, ansichtdefid, tabdefid)
values('FX_Menues_K', 3, 'FX_AnsichtDef_K', 
'left', 'DSID', 'FX_Menues_K', 'AnsichtDefId', 
'MW#migration#', '03.03.15', 'X', -1, 
(select max(at.DSID)+1 from FX_AnsichtTab_K at ),
(select ad.dsid from FX_AnsichtDef_K ad where ad.ansicht = 'FX_Menues_K'),
(select td.dsid from FX_TabDef_K td where tabelle = 'FX_AnsichtDef_K')
);

update FX_AnsichtDef_K 
set reihenfolge  = 5, bereich = 'Administrator', urlpath = 'benutzerBereich', appcontextpath = 'admin' 
where ansicht = 'BenutzerBereicheDef';

update FX_AnsichtTab_K set JOIN1SPALTEAKEY = 'BereichsId' 
where ansicht = 'FX_TabDef_K' and tabakey = 'BenutzerBereicheDef';

update FX_AnsichtSpalten_K set ingridanzeigen = -1
where ansicht = 'FX_TabDef_K' and spalteakey = 'BereichsId';

-- fix PS in FX_TabSpEig_K, tabelle FX_TabSpEig_K
update FX_TabSpEig_K set ps = 0 where tabelle = 'FX_TabSpEig_K' and spalte = 'TabDefId';

commit;

-- ADANSICHTSPALTEN.Reihenfolge hinzuf�gen und bef�llen
alter table ADANSICHTSPALTEN add  REIHENFOLGE number(3,0);

update ADANSICHTSPALTEN adSpalten 
set adSpalten.Reihenfolge = (
  select aSpalten.INDEXGRID
  from FX_AnsichtSpalten_K aSpalten
  where aSpalten.DSID = adSpalten.ANSICHTSPALTENDEFID
)
where adSpalten.REIHENFOLGE is null
;

alter table ADANSICHTSPALTEN modify  reihenfolge number(3,0) not null;

alter table FX_AnsichtOrderBy_K
add	 CONSTRAINT "FK_FX_AnsOrderBy_ANSICHTDEFID" FOREIGN KEY ("ANSICHTDEFID")
	  REFERENCES "FX_ANSICHTDEF_K" ("DSID") ENABLE; 
    
CREATE UNIQUE INDEX "UQ_FX_ANSICHTDEF_K_E2" ON "FX_ANSICHTDEF_K" (urlpath);
  
update FX_TabSpEig_K set defaultwert = '#mwdata#now' where upper(defaultwert) = '#FIRSTX#JETZT';

update FX_TabSpEig_K 
set defaultwert = '#mwdata#userid' where upper(defaultwert) = '#FIRSTX#BENUTZERID';

-- set constraints for FX_AnsichtOrderBy_K
alter table FX_AnsichtOrderBy_K add TabDefId number(11,0);

-- AnsichtOrderBy.tabakey grosskleinschreibung angleichen mit ansichttab
update FX_AnsichtOrderBy_K ao
set ao.tabakey = (
  select at.tabakey
  from FX_AnsichtTab_K at
  where upper(at.tabakey) = upper(ao.TABAKEY)
  and at.ansichtdefid = ao.ansichtdefid
) 
where exists(
  select 1 
  from FX_AnsichtTab_K at2
  where upper(at2.tabakey) = upper(ao.TABAKEY)
  and at2.ansichtdefid = ao.ansichtdefid
)
;
  
-- jetzt noch spalte tabdefid von ansichtOrderBy updaten
update FX_AnsichtOrderBy_K ao
set ao.TABDEFID = (
  select at.tabdefid
  from FX_AnsichtTab_K at
  where ao.ANSICHTDEFID = at.ANSICHTDEFID
  and ao.tabakey = at.tabakey
)
;

update FX_AnsichtOrderBy_K ao
set ao.spalteakey = (
  select ass.spalteakey
  from FX_AnsichtSpalten_K ass
  where ass.ansichtdefid = ao.ansichtdefid
  and upper(ass.spalteakey) = upper(ao.spalteakey)
  and ass.tabakey = ao.tabakey
)
where exists(
  select 1
  from FX_AnsichtSpalten_K ass2
  where ass2.ansichtdefid = ao.ansichtdefid
  and upper(ass2.spalteakey) = upper(ao.spalteakey)
  and ass2.tabakey = ao.tabakey
)
;

commit;

alter table FX_AnsichtOrderBy_K
add	 CONSTRAINT "FK_FX_AnsOrder_TABDEFID" FOREIGN KEY ("TABDEFID")
	  REFERENCES FX_TabDef_K (DSID) ENABLE; 
  
-- cleanup feld ansichtspalten.tabakey    
update FX_AnsichtSpalten_K asp
set asp.tabakey = (
  select at.tabakey
  from FX_AnsichtTAb_K at
  where upper(asp.tabakey) = upper(at.tabakey)
  and asp.ansichtdefid = at.ansichtdefid
  and asp.ansicht = at.ansicht
);    
    
commit;
    
alter table FX_AnsichtSpalten_K add ANSICHTTABID number(11,0);

update FX_AnsichtSpalten_K asp
set asp.ansichtTabId = (
  select at.dsid
  from FX_AnsichtTab_K at
  where at.tabakey = asp.tabakey
  and at.ansichtdefid = asp.ansichtdefid
);
commit;

alter table FX_AnsichtSpalten_K add TABSPEIGID number(11,0);

-- update ansichtspalten.tabspeigid wo spalteakey = tabspeig.spalte -- dauert recht lange ...
update FX_AnsichtSpalten_K asp
set asp.tabspeigid = (
  select ts.dsid
  from FX_AnsichtTab_K at, FX_TabDef_K t, FX_TAbSpeig_K ts
  where 1=1
  and at.dsid = asp.ansichttabid
  and at.TABDEFID = t.DSID
  and t.dsid = ts.TABDEFID
  and upper(ts.spalte) = upper(asp.spalteakey)
)
where exists(
  select 1
  from FX_AnsichtTab_K at, FX_TabDef_K t, FX_TAbSpeig_K ts
  where 1=1
  and at.dsid = asp.ansichttabid
  and at.TABDEFID = t.DSID
  and t.dsid = ts.TABDEFID
  and upper(ts.spalte) = upper(asp.spalteakey)
);
commit;

-- update ansichtspalten.tabspeigid wo spalte = tabspeig.spalte
update FX_AnsichtSpalten_K asp
set asp.tabspeigid = (
  select ts.dsid
  from FX_AnsichtTab_K at, FX_TabDef_K t, FX_TAbSpeig_K ts
  where 1=1
  and at.dsid = asp.ansichttabid
  and at.TABDEFID = t.DSID
  and t.dsid = ts.TABDEFID
  and upper(ts.spalte) = upper(asp.spalte)
)
where asp.tabspeigid is null
and exists(
  select 1
  from FX_AnsichtTab_K at, FX_TabDef_K t, FX_TAbSpeig_K ts
  where 1=1
  and at.dsid = asp.ansichttabid
  and at.TABDEFID = t.DSID
  and t.dsid = ts.TABDEFID
  and upper(ts.spalte) = upper(asp.spalte)
);
commit;

alter table FX_AnsichtSpalten_K
add	 CONSTRAINT "FK_FX_AnsSp_ANSICHTTABID" FOREIGN KEY ("ANSICHTTABID")
	  REFERENCES FX_AnsichtTab_K (DSID) ENABLE;
    
alter table FX_AnsichtSpalten_K
add	 CONSTRAINT "FK_FX_AnsSp_TABSPEIGID" FOREIGN KEY ("TABSPEIGID")
	  REFERENCES FX_TabSpEig_K (DSID) ENABLE;
    
-- fix ANSICHTORDERBY, do reference by ANSICHTTAB and ANSICHTSPALTEN
alter table FX_AnsichtOrderBy_K add ANSICHTTABID number(11,0);

alter table FX_AnsichtOrderBy_K add ANSICHTSPALTENID number(11,0);

update FX_AnsichtOrderBy_K ao
set ao.ansichtTabId = (
  select at.dsid
  from FX_AnsichtTab_K at
  where upper(at.tabakey) = upper(ao.TABAKEY)
  and at.ansichtdefid = ao.ansichtdefid
) 
where exists(
  select 1 
  from FX_AnsichtTab_K at2
  where upper(at2.tabakey) = upper(ao.TABAKEY)
  and at2.ansichtdefid = ao.ansichtdefid
)
;

update FX_AnsichtOrderBy_K ao
set ao.ANSICHTSPALTENID = (
  select sp.dsid
  from FX_AnsichtDef_K ad
  join FX_AnsichtTab_K at on (ad.dsid = at.ansichtdefid ) 
  join FX_AnsichtSpalten_K sp on (sp.ansichttabid = at.dsid)
  where ad.dsid = ao.ansichtdefid
  and at.dsid = ao.ansichttabid
  and upper(sp.spalteakey) = upper(ao.spalteakey)
)
where exists(
select 1
  from FX_AnsichtDef_K adInner
  join FX_AnsichtTab_K atInner on (adInner.dsid = atInner.ansichtdefid ) 
  join FX_AnsichtSpalten_K spInner on (spInner.ansichttabid = atInner.dsid)
  where adInner.dsid = ao.ansichtdefid
  and atInner.dsid = ao.ansichttabid
  and upper(spInner.spalteakey) = upper(ao.spalteakey)
);
commit;

alter table FX_AnsichtOrderBY_K
add	 CONSTRAINT "FK_FX_AnsOrd_ANSICHTTABID" FOREIGN KEY ("ANSICHTTABID")
	  REFERENCES FX_AnsichtTab_K (DSID) ENABLE;
    
alter table FX_AnsichtOrderBY_K modify  ANSICHTTABID number(11,0) not null;

alter table FX_AnsichtOrderBY_K
add	 CONSTRAINT "FK_FX_AnsOrd_ANSICHTSPALTENID" FOREIGN KEY ("ANSICHTSPALTENID")
	  REFERENCES FX_AnsichtSpalten_K (DSID) ENABLE;
    
alter table FX_AnsichtOrderBY_K modify  ANSICHTSPALTENID number(11,0) not null;

alter table FX_AnsichtOrderBY_K drop column ansichtdefid;

alter table FX_AnsichtOrderBY_K drop column tabdefid;

-- add ofdb column because of setting AbstractMWOFDBentity deprecated
alter table BenutzerBereicheDef add ofdb varchar2(255) null;
update BenutzerBereicheDef set ofdb = 'X';
alter table BenutzerBereicheDef modify ofdb varchar2(255) not null;

alter table BENUTZERDEF add ofdb varchar2(255) null;
update BENUTZERDEF set ofdb = 'X';
alter table BENUTZERDEF modify ofdb varchar2(255) not null;

-- system 
alter table BENUTZERDEF add system numeric(1) default -1 not null;
alter table BENUTZERBEREICHEDEF add system numeric(1) default -1 not null;

alter table BenutzerRechte add ofdb varchar2(255) null;
update BenutzerRechte set ofdb = 'X';
alter table BenutzerRechte modify ofdb varchar2(255) not null;
alter table BenutzerRechte add system numeric(1) default -1 not null;


alter table SysSequenz add ofdb varchar2(255) null;
update SysSequenz set ofdb = 'X';
alter table SysSequenz modify ofdb varchar2(255) not null;
alter table SysSequenz add system numeric(1) default -1 not null;

alter table ADANSICHTENDEF modify angelegtvon varchar2(255);
