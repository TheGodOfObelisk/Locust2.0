/*==============================================================*/
/* DBMS name:      MySQL 5.0                                    */
/* Created on:     2023/2/27 20:45:55                           */
/*==============================================================*/


drop table if exists Alternate_Term;

drop table if exists Alternate_Terms;

drop table if exists Attack_Pattern;

drop table if exists Attack_Pattern_Catalog;

drop table if exists Attack_Patterns;

drop table if exists Attack_Steps;

drop table if exists Audience;

drop table if exists Author;

drop table if exists Categories;

drop table if exists Category;

drop table if exists Consequence;

drop table if exists Consequences;

drop table if exists Content_History;

drop table if exists Contribution;

drop table if exists Contributions;

drop table if exists Descriptions;

drop table if exists Example;

drop table if exists Example_Instances;

drop table if exists Exclude_Related;

drop table if exists Execution_Flow;

drop table if exists Extended_Descriptions;

drop table if exists External_Reference;

drop table if exists Impacts;

drop table if exists Indicators;

drop table if exists Member;

drop table if exists Members;

drop table if exists Mitigation;

drop table if exists Mitigations;

drop table if exists Modification;

drop table if exists Note;

drop table if exists Notes;

drop table if exists Objective;

drop table if exists Prerequisites;

drop table if exists Previous_Entry_Name;

drop table if exists Previous_Entry_Names;

drop table if exists Reference;

drop table if exists ReferencesT;

drop table if exists Related_Attack_Pattern;

drop table if exists Related_Attack_Patterns;

drop table if exists Related_Weakness;

drop table if exists Related_Weaknesses;

drop table if exists Relationships;

drop table if exists Resources_Required;

drop table if exists Scopes;

drop table if exists Skills_Required;

drop table if exists Submission;

drop table if exists Taxonomy_Mapping;

drop table if exists Taxonomy_Mappings;

drop table if exists Techniques;

drop table if exists View;

/*==============================================================*/
/* Table: Alternate_Term                                        */
/*==============================================================*/
create table Alternate_Term
(
   ID                   char(36) not null,
   Term                 char(50),
   Descriptions_ID      char(36),
   primary key (ID),
   unique key AK_Key_2 (Descriptions_ID)
);

/*==============================================================*/
/* Table: Alternate_Terms                                       */
/*==============================================================*/
create table Alternate_Terms
(
   ID                   char(36) not null,
   Alternate_Terms_ID   char(36) not null,
   Alternate_Term_ID    char(36),
   primary key (ID),
   unique key AK_Key_2 (Alternate_Term_ID)
);

/*==============================================================*/
/* Table: Attack_Pattern                                        */
/*==============================================================*/
create table Attack_Pattern
(
   ID                   char(36) not null,
   Name                 char(50),
   References_ID        char(36),
   Abstraction          char(50),
   Descriptions_ID      char(36),
   Extended_Descriptions_ID char(36),
   Alternate_Terms_ID   char(36),
   Likelihood_Of_Attack char(50),
   Typical_Severity     char(50),
   Related_Attack_Patterns_ID char(36),
   Execution_Flow_ID    char(36),
   Prerequisites_ID     char(36),
   Skills_Required_ID   char(36),
   Resources_Required_ID char(36),
   Indicators_ID        char(36),
   Consequences_ID      char(36),
   Mitigations_ID       char(36),
   Example_Instances_ID char(36),
   Related_Weaknesses_ID char(36),
   Taxonomy_Mappings_ID char(36),
   Notes_ID             char(36),
   Content_History_ID   char(36),
   primary key (ID),
   unique key AK_Key_Alternate_Terms_ID (Alternate_Terms_ID),
   unique key AK_Key_Consequences_ID (Consequences_ID),
   unique key AK_Key_Content_History_ID (Content_History_ID),
   unique key AK_Key_Descriptions_ID (Descriptions_ID),
   unique key AK_Key_Example_Instances_ID (Example_Instances_ID),
   unique key AK_Key_Execution_Flow_ID (Execution_Flow_ID),
   unique key AK_Key_Extended_Descriptions_ID (Extended_Descriptions_ID),
   unique key AK_Key_Indicators_ID (Indicators_ID),
   unique key AK_Key_Mitigations_ID (Mitigations_ID),
   unique key AK_Key_Notes_ID (Notes_ID),
   unique key AK_Key_Prerequisites_ID (Prerequisites_ID),
   unique key AK_Key_Related_Attack_Patterns_ID (Related_Attack_Patterns_ID),
   unique key AK_Key_Related_Weaknesses_ID (Related_Weaknesses_ID),
   unique key AK_Key_Resources_Required_ID (Resources_Required_ID),
   unique key AK_Key_Skills_Required_ID (Skills_Required_ID),
   unique key AK_Key_Taxonomy_Mappings_ID (Taxonomy_Mappings_ID)
);

/*==============================================================*/
/* Table: Attack_Pattern_Catalog                                */
/*==============================================================*/
create table Attack_Pattern_Catalog
(
   ID                   char(36) not null,
   Name                 varchar(20),
   Version              varchar(20),
   Date                 date,
   Attack_Patterns_ID   char(36),
   Categories_ID        char(36),
   Views_ID             char(36),
   External_References_ID char(36),
   primary key (ID),
   unique key AK_Key_Attack_Patterns_ID (Attack_Patterns_ID),
   unique key AK_Key_Categories_ID (Categories_ID),
   unique key AK_Key_External_References_ID (External_References_ID),
   unique key AK_Key_Views_ID (Views_ID)
);

/*==============================================================*/
/* Table: Attack_Patterns                                       */
/*==============================================================*/
create table Attack_Patterns
(
   ID                   char(36) not null,
   Attack_Patterns_ID   char(36) not null,
   Attack_Pattern_ID    char(36),
   primary key (ID),
   unique key AK_Key_Attack_Pattern_ID (Attack_Pattern_ID)
);

/*==============================================================*/
/* Table: Attack_Steps                                          */
/*==============================================================*/
create table Attack_Steps
(
   ID                   char(36) not null,
   Attack_Steps_ID      char(36) not null,
   Step                 integer,
   Phase                char(50),
   Techniques_ID        char(36),
   Descriptions_ID      char(36),
   primary key (ID),
   unique key AK_Key_Descriptions_ID (Descriptions_ID),
   unique key AK_Key_Techniques_ID (Techniques_ID)
);

/*==============================================================*/
/* Table: Audience                                              */
/*==============================================================*/
create table Audience
(
   ID                   char(36) not null,
   Stakeholder          char(255),
   Type                 char(50),
   Description          char(255),
   Audience_ID          char(36) not null,
   primary key (ID)
);

/*==============================================================*/
/* Table: Author                                                */
/*==============================================================*/
create table Author
(
   ID                   char(36) not null,
   Authors_ID           char(36) not null,
   Author               char(50),
   primary key (ID)
);

/*==============================================================*/
/* Table: Categories                                            */
/*==============================================================*/
create table Categories
(
   ID                   char(36) not null,
   Category_ID          char(36),
   Categories_ID        char(36) not null,
   primary key (ID),
   unique key AK_Key_2 (Category_ID)
);

/*==============================================================*/
/* Table: Category                                              */
/*==============================================================*/
create table Category
(
   ID                   char(36) not null,
   Name                 char(50),
   Status               char(50),
   Summary              char(255),
   Relationships_ID     char(36),
   Taxonomy_Mappings_ID char(36),
   References_ID        char(36),
   Notes_ID             char(36),
   Content_History_ID   char(36),
   primary key (ID),
   unique key AK_Key_Content_History_ID (Content_History_ID),
   unique key AK_Key_Notes_ID (Notes_ID),
   unique key AK_Key_References_ID (References_ID),
   unique key AK_Key_Relationships_ID (Relationships_ID),
   unique key AK_Key_Taxonomy_Mappings_ID (Taxonomy_Mappings_ID)
);

/*==============================================================*/
/* Table: Consequence                                           */
/*==============================================================*/
create table Consequence
(
   ID                   char(36) not null,
   Scopes_ID            char(36),
   Impacts_ID           char(36),
   Likelihood           char(50),
   Note_ID              char(36),
   primary key (ID),
   unique key AK_Key_Impacts_ID (Impacts_ID),
   unique key AK_Key_Note_ID (Note_ID),
   unique key AK_Key_Scopes_ID (Scopes_ID)
);

/*==============================================================*/
/* Table: Consequences                                          */
/*==============================================================*/
create table Consequences
(
   ID                   char(36) not null,
   Consequences_ID      char(36) not null,
   Consequence_ID       char(36),
   primary key (ID),
   unique key AK_Key_2 (Consequence_ID)
);

/*==============================================================*/
/* Table: Content_History                                       */
/*==============================================================*/
create table Content_History
(
   ID                   char(36) not null,
   Submission_ID        char(36),
   Modification_ID      char(36),
   Contributions_ID     char(36),
   Previous_Entry_Names_ID char(36),
   primary key (ID),
   unique key AK_Key_Contributions_ID (Contributions_ID),
   unique key AK_Key_Modification_ID (Modification_ID),
   unique key AK_Key_Previous_Entity_Names_ID (Previous_Entry_Names_ID),
   unique key AK_Key_Submission_ID (Submission_ID)
);

/*==============================================================*/
/* Table: Contribution                                          */
/*==============================================================*/
create table Contribution
(
   ID                   char(36) not null,
   Type                 char(50),
   Contribution_Name    char(255),
   Contribution_Organization char(255),
   Contribution_Date    date,
   Contribution_Comment char(255),
   primary key (ID)
);

/*==============================================================*/
/* Table: Contributions                                         */
/*==============================================================*/
create table Contributions
(
   ID                   char(36) not null,
   Contributions_ID     char(36) not null,
   Contribution_ID      char(36),
   primary key (ID),
   unique key AK_Key_2 (Contribution_ID)
);

/*==============================================================*/
/* Table: Descriptions                                          */
/*==============================================================*/
create table Descriptions
(
   ID                   char(36) not null,
   Descriptions_ID      char(36) not null,
   Description          char(50),
   Description_Content  text,
   primary key (ID)
);

/*==============================================================*/
/* Table: Example                                               */
/*==============================================================*/
create table Example
(
   ID                   char(36) not null,
   Example_Content      text,
   primary key (ID)
);

/*==============================================================*/
/* Table: Example_Instances                                     */
/*==============================================================*/
create table Example_Instances
(
   ID                   char(36) not null,
   Example_Instances_ID char(36) not null,
   Example_ID           char(36),
   primary key (ID),
   unique key AK_Key_2 (Example_ID)
);

/*==============================================================*/
/* Table: Exclude_Related                                       */
/*==============================================================*/
create table Exclude_Related
(
   ID                   char(36) not null,
   Exclude_ID           integer,
   Exclude_Related_ID   char(36) not null,
   primary key (ID)
);

/*==============================================================*/
/* Table: Execution_Flow                                        */
/*==============================================================*/
create table Execution_Flow
(
   ID                   char(36) not null,
   Execution_Flow_ID    char(36) not null,
   Attack_Steps_ID      char(36),
   primary key (ID),
   unique key AK_Key_2 (Attack_Steps_ID)
);

/*==============================================================*/
/* Table: Extended_Descriptions                                 */
/*==============================================================*/
create table Extended_Descriptions
(
   ID                   char(36) not null,
   Extended_Descrptions_ID char(36) not null,
   Extended_Description char(50),
   Extended_Description_Content text,
   primary key (ID)
);

/*==============================================================*/
/* Table: External_Reference                                    */
/*==============================================================*/
create table External_Reference
(
   ID                   char(36) not null,
   Authors_ID           char(50),
   Title                char(50),
   Edition              char(50),
   Publication          char(50),
   Publication_Year     char(20),
   Publication_Month    char(20),
   Publication_Day      char(20),
   Publisher            char(50),
   URL                  char(255),
   URL_Date             date,
   External_Reference_ID char(36) not null,
   primary key (ID),
   unique key AK_Key_2 (Authors_ID)
);

/*==============================================================*/
/* Table: Impacts                                               */
/*==============================================================*/
create table Impacts
(
   ID                   char(36) not null,
   Impacts_ID           char(36) not null,
   Impact               char(50),
   primary key (ID)
);

/*==============================================================*/
/* Table: Indicators                                            */
/*==============================================================*/
create table Indicators
(
   ID                   char(36) not null,
   Indicators_ID        char(36) not null,
   Indicator            text,
   primary key (ID)
);

/*==============================================================*/
/* Table: Member                                                */
/*==============================================================*/
create table Member
(
   ID                   char(36) not null,
   CAPEC_ID             char(100),
   Exclude_Related_ID   char(36),
   Member_ID            char(36),
   primary key (ID),
   unique key AK_Key_Exclude_Related_ID (Exclude_Related_ID),
   unique key AK_Key_Member_ID (Member_ID)
);

/*==============================================================*/
/* Table: Members                                               */
/*==============================================================*/
create table Members
(
   ID                   varchar(36) not null,
   Has_Member_ID        char(36) not null,
   Member_Of_ID         char(36),
   primary key (ID),
   unique key AK_Key_Has_Member_ID (Has_Member_ID),
   unique key AK_Key_Member_Of_ID (Member_Of_ID)
);

/*==============================================================*/
/* Table: Mitigation                                            */
/*==============================================================*/
create table Mitigation
(
   ID                   char(36) not null,
   Mitigation_Content   text,
   primary key (ID)
);

/*==============================================================*/
/* Table: Mitigations                                           */
/*==============================================================*/
create table Mitigations
(
   ID                   char(36) not null,
   Mitigations_ID       char(36) not null,
   Mitigation_ID        char(36),
   primary key (ID),
   unique key AK_Key_Mitigation_ID (Mitigation_ID)
);

/*==============================================================*/
/* Table: Modification                                          */
/*==============================================================*/
create table Modification
(
   ID                   char(36) not null,
   Modification_Name    char(255),
   Modification_Organization char(255),
   Modification_Date    date,
   Modification_Importance char(50),
   Modification_Comment char(255),
   primary key (ID)
);

/*==============================================================*/
/* Table: Note                                                  */
/*==============================================================*/
create table Note
(
   ID                   char(36) not null,
   Type                 char(50),
   Content              text,
   primary key (ID)
);

/*==============================================================*/
/* Table: Notes                                                 */
/*==============================================================*/
create table Notes
(
   ID                   char(36) not null,
   Notes_ID             char(36) not null,
   Note_ID              char(36),
   primary key (ID),
   unique key AK_Key_Note_ID (Note_ID)
);

/*==============================================================*/
/* Table: Objective                                             */
/*==============================================================*/
create table Objective
(
   ID                   char(36) not null,
   Objective            char(200),
   Objective_ID         char(36) not null,
   primary key (ID, Objective_ID)
);

/*==============================================================*/
/* Table: Prerequisites                                         */
/*==============================================================*/
create table Prerequisites
(
   ID                   char(36) not null,
   Prerequisites_ID     char(36) not null,
   Prerequisite         char(50),
   Prerequisite_Content text,
   primary key (ID)
);

/*==============================================================*/
/* Table: Previous_Entry_Name                                   */
/*==============================================================*/
create table Previous_Entry_Name
(
   ID                   char(36) not null,
   Date                 date,
   Previous_Entry_Name  char(255),
   primary key (ID)
);

/*==============================================================*/
/* Table: Previous_Entry_Names                                  */
/*==============================================================*/
create table Previous_Entry_Names
(
   ID                   char(36) not null,
   Previous_Entry_Names_ID char(36) not null,
   Previous_Entry_Name_ID char(36),
   primary key (ID),
   unique key AK_Key_Previous_Entity_Name_ID (Previous_Entry_Name_ID)
);

/*==============================================================*/
/* Table: Reference                                             */
/*==============================================================*/
create table Reference
(
   ID                   char(36) not null,
   External_Reference_ID char(100),
   Section              char(100),
   primary key (ID)
);

/*==============================================================*/
/* Table: ReferencesT                                           */
/*==============================================================*/
create table ReferencesT
(
   ID                   char(36) not null,
   References_ID        char(36) not null,
   Reference_ID         char(36),
   primary key (ID),
   unique key AK_Key_Reference_ID (Reference_ID)
);

alter table ReferencesT comment 'Since "References" conflicts with the keyword of MySQL, use ';

/*==============================================================*/
/* Table: Related_Attack_Pattern                                */
/*==============================================================*/
create table Related_Attack_Pattern
(
   ID                   char(36) not null,
   Nature               char(50),
   CAPEC_ID             integer,
   Exclude_Related_ID   char(36),
   primary key (ID),
   unique key AK_Key_Exclude_Related_ID (Exclude_Related_ID)
);

/*==============================================================*/
/* Table: Related_Attack_Patterns                               */
/*==============================================================*/
create table Related_Attack_Patterns
(
   ID                   char(36) not null,
   Related_Attack_Patterns_ID char(36) not null,
   Related_Attack_Pattern_ID char(36),
   primary key (ID),
   unique key AK_Key_Related_Attack_Pattern_ID (Related_Attack_Pattern_ID)
);

/*==============================================================*/
/* Table: Related_Weakness                                      */
/*==============================================================*/
create table Related_Weakness
(
   ID                   char(36) not null,
   CWE_ID               integer,
   primary key (ID)
);

/*==============================================================*/
/* Table: Related_Weaknesses                                    */
/*==============================================================*/
create table Related_Weaknesses
(
   ID                   char(36) not null,
   Related_Weaknesses_ID char(36) not null,
   Related_Weakness_ID  char(36),
   primary key (ID),
   unique key AK_Key_Related_Weakness_ID (Related_Weakness_ID)
);

/*==============================================================*/
/* Table: Relationships                                         */
/*==============================================================*/
create table Relationships
(
   ID                   char(36) not null,
   Relationships_ID     char(36) not null,
   Member_Of_ID         char(36),
   Has_Member_ID        char(36),
   primary key (ID),
   unique key AK_Key_Has_Member_ID (Has_Member_ID),
   unique key AK_Key_Member_Of_ID (Member_Of_ID)
);

/*==============================================================*/
/* Table: Resources_Required                                    */
/*==============================================================*/
create table Resources_Required
(
   ID                   char(36) not null,
   Resources_Required_ID char(36) not null,
   Resource             text,
   primary key (ID)
);

/*==============================================================*/
/* Table: Scopes                                                */
/*==============================================================*/
create table Scopes
(
   ID                   char(36) not null,
   Scopes_ID            char(36) not null,
   Scope                char(50),
   primary key (ID)
);

/*==============================================================*/
/* Table: Skills_Required                                       */
/*==============================================================*/
create table Skills_Required
(
   ID                   char(36) not null,
   Skills_Required_ID   char(36) not null,
   Skill                text,
   Level                char(50),
   primary key (ID)
);

/*==============================================================*/
/* Table: Submission                                            */
/*==============================================================*/
create table Submission
(
   ID                   char(36) not null,
   Submission_Name      char(255),
   Submission_Organization char(255),
   Submission_Date      date,
   Submission_Comment   char(255),
   primary key (ID)
);

/*==============================================================*/
/* Table: Taxonomy_Mapping                                      */
/*==============================================================*/
create table Taxonomy_Mapping
(
   ID                   char(36) not null,
   Taxonomy_Name        char(50),
   Entry_ID             char(50),
   Mapping_Fit          char(50),
   primary key (ID)
);

/*==============================================================*/
/* Table: Taxonomy_Mappings                                     */
/*==============================================================*/
create table Taxonomy_Mappings
(
   ID                   char(36) not null,
   Taxonomy_Mappings_ID char(36) not null,
   Taxonomy_Mapping_ID  char(36),
   primary key (ID),
   unique key AK_Key_Taxonomy_Mapping_ID (Taxonomy_Mapping_ID)
);

/*==============================================================*/
/* Table: Techniques                                            */
/*==============================================================*/
create table Techniques
(
   ID                   char(36) not null,
   Techniques_ID        char(36) not null,
   Technique            char(50),
   CAPEC_ID             char(50),
   Technique_Content    text,
   primary key (ID)
);

/*==============================================================*/
/* Table: View                                                  */
/*==============================================================*/
create table View
(
   ID                   char(36) not null,
   Name                 char(50),
   Type                 char(50),
   Status               char(50),
   Objective_ID         char(36),
   Audience_ID          char(36),
   Members_ID           char(36),
   Filter               char(200),
   References_ID        char(36),
   Notes_ID             char(36),
   Content_History_ID   char(36),
   View_ID              char(36) not null,
   primary key (ID),
   unique key AK_Key_Content_History_ID (Content_History_ID),
   unique key AK_Key_Members_ID (Members_ID),
   unique key AK_Key_Audience_ID (Audience_ID),
   unique key AK_Key_Notes_ID (Notes_ID),
   unique key AK_Key_Objective_ID (Objective_ID),
   unique key AK_Key_References_ID (References_ID)
);

alter table Alternate_Term add constraint FK_T_Alternate_Term_2_Alternate_Terms_1 foreign key (ID)
      references Alternate_Terms (Alternate_Term_ID) on delete restrict on update restrict;

alter table Alternate_Terms add constraint FK_T_Alternate_Terms_2_Attack_Pattern_1 foreign key (Alternate_Terms_ID)
      references Attack_Pattern (Alternate_Terms_ID) on delete restrict on update restrict;

alter table Attack_Pattern add constraint FK_T_Attack_Pattern_2_Attack_Patterns_1 foreign key (ID)
      references Attack_Patterns (Attack_Pattern_ID) on delete restrict on update restrict;

alter table Attack_Pattern add constraint FK_T_Related_Attack_Pattern_2_Related_Attack_Patterns_2 foreign key (ID)
      references Attack_Patterns (Attack_Pattern_ID) on delete restrict on update restrict;

alter table Attack_Patterns add constraint FK_T_Attack_Patterns_2_Attack_Pattern_Catalog_1 foreign key (Attack_Patterns_ID)
      references Attack_Pattern_Catalog (Attack_Patterns_ID) on delete restrict on update restrict;

alter table Attack_Steps add constraint FK_T_Attack_Steps_2_Execution_Flow_1 foreign key (Attack_Steps_ID)
      references Execution_Flow (Attack_Steps_ID) on delete restrict on update restrict;

alter table Audience add constraint FK_T_Audience_2_View_1 foreign key (Audience_ID)
      references View (Audience_ID) on delete restrict on update restrict;

alter table Author add constraint FK_T_Author_2_External_Reference_1 foreign key (Authors_ID)
      references External_Reference (Authors_ID) on delete restrict on update restrict;

alter table Categories add constraint FK_T_Categories_2_Attack_Pattern_Category_Category_1 foreign key (Categories_ID)
      references Attack_Pattern_Catalog (Categories_ID) on delete restrict on update restrict;

alter table Category add constraint FK_T_Category_2_Categories_1 foreign key (ID)
      references Categories (Category_ID) on delete restrict on update restrict;

alter table Consequence add constraint FK_T_Consequence_2_Consequences_1 foreign key (ID)
      references Consequences (Consequence_ID) on delete restrict on update restrict;

alter table Consequences add constraint FK_T_Consequences_2_Attack_Pattern_1 foreign key (Consequences_ID)
      references Attack_Pattern (Consequences_ID) on delete restrict on update restrict;

alter table Content_History add constraint FK_T_Content_History_2_Attack_Pattern_1 foreign key (ID)
      references Attack_Pattern (Content_History_ID) on delete restrict on update restrict;

alter table Content_History add constraint FK_T_Content_History_2_Category_1 foreign key (ID)
      references Category (Content_History_ID) on delete restrict on update restrict;

alter table Content_History add constraint FK_T_Content_History_2_View_1 foreign key (ID)
      references View (Content_History_ID) on delete restrict on update restrict;

alter table Contribution add constraint FK_T_Contributions_2_Contributions_1 foreign key (ID)
      references Contributions (Contribution_ID) on delete restrict on update restrict;

alter table Contributions add constraint FK_T_Contributions_2_Content_History_1 foreign key (Contributions_ID)
      references Content_History (Contributions_ID) on delete restrict on update restrict;

alter table Descriptions add constraint FK_T_Descriptions_2_Alternate_Term_1 foreign key (Descriptions_ID)
      references Alternate_Term (Descriptions_ID) on delete restrict on update restrict;

alter table Descriptions add constraint FK_T_Descriptions_2_Attack_Pattern_1 foreign key (Descriptions_ID)
      references Attack_Pattern (Descriptions_ID) on delete restrict on update restrict;

alter table Descriptions add constraint FK_T_Descriptions_2_Attack_Steps_1 foreign key (ID)
      references Attack_Steps (Descriptions_ID) on delete restrict on update restrict;

alter table Example add constraint FK_T_Example_2_Example_Instances_1 foreign key (ID)
      references Example_Instances (Example_ID) on delete restrict on update restrict;

alter table Example_Instances add constraint FK_T_Example_Instances_2_Attack_Pattern_1 foreign key (Example_Instances_ID)
      references Attack_Pattern (Example_Instances_ID) on delete restrict on update restrict;

alter table Exclude_Related add constraint FK_T_Exclude_Related_2_Member_1 foreign key (Exclude_Related_ID)
      references Member (Exclude_Related_ID) on delete restrict on update restrict;

alter table Exclude_Related add constraint FK_T_Exclude_Related_2_Related_Attack_Pattern_1 foreign key (Exclude_Related_ID)
      references Related_Attack_Pattern (Exclude_Related_ID) on delete restrict on update restrict;

alter table Execution_Flow add constraint FK_T_Execution_Flow_2_Attack_Pattern_1 foreign key (Execution_Flow_ID)
      references Attack_Pattern (Execution_Flow_ID) on delete restrict on update restrict;

alter table Extended_Descriptions add constraint FK_T_Extended_Descriptions_2_Attack_Pattern_1 foreign key (Extended_Descrptions_ID)
      references Attack_Pattern (Extended_Descriptions_ID) on delete restrict on update restrict;

alter table External_Reference add constraint FK_T_External_Reference_2_Attack_Pattern_Catalog_1 foreign key (External_Reference_ID)
      references Attack_Pattern_Catalog (External_References_ID) on delete restrict on update restrict;

alter table Impacts add constraint FK_T_Impacts_2_Consequnece_1 foreign key (Impacts_ID)
      references Consequence (Impacts_ID) on delete restrict on update restrict;

alter table Indicators add constraint FK_T_Indicators_2_Attack_Pattern_1 foreign key (Indicators_ID)
      references Attack_Pattern (Indicators_ID) on delete restrict on update restrict;

alter table Member add constraint FK_T_Member_2_Relationships foreign key (Member_ID)
      references Relationships (Member_Of_ID) on delete restrict on update restrict;

alter table Member add constraint FK_T_Members_2_Member_1 foreign key (Member_ID)
      references Members (Has_Member_ID) on delete restrict on update restrict;

alter table Member add constraint FK_T_Members_2_Member_ID_1 foreign key (Member_ID)
      references Members (Member_Of_ID) on delete restrict on update restrict;

alter table Member add constraint FK_T_Relationships_2_Members_1 foreign key (Member_ID)
      references Relationships (Has_Member_ID) on delete restrict on update restrict;

alter table Members add constraint FK_T_Members_2_View_C_ID_1 foreign key (ID)
      references View (Members_ID) on delete restrict on update restrict;

alter table Mitigation add constraint FK_T_Mitigation_2_Mitigation_1 foreign key (ID)
      references Mitigations (Mitigation_ID) on delete restrict on update restrict;

alter table Mitigations add constraint FK_T_Mitigations_2_Attack_Pattern_1 foreign key (Mitigations_ID)
      references Attack_Pattern (Mitigations_ID) on delete restrict on update restrict;

alter table Modification add constraint FK_T_Modification_2_Content_History_1 foreign key (ID)
      references Content_History (Modification_ID) on delete restrict on update restrict;

alter table Note add constraint FK_T_Note_2_Consequence_1 foreign key (ID)
      references Consequence (Note_ID) on delete restrict on update restrict;

alter table Note add constraint FK_T_Notes_2_Note_1 foreign key (ID)
      references Notes (Note_ID) on delete restrict on update restrict;

alter table Notes add constraint FK_T_Notes_2_Attack_Pattern_1 foreign key (Notes_ID)
      references Attack_Pattern (Notes_ID) on delete restrict on update restrict;

alter table Notes add constraint FK_T_Notes_2_Category_1 foreign key (Notes_ID)
      references Category (Notes_ID) on delete restrict on update restrict;

alter table Notes add constraint FK_T_Notes_2_View_1 foreign key (Notes_ID)
      references View (Notes_ID) on delete restrict on update restrict;

alter table Objective add constraint FK_T_Objective_2_View_1 foreign key (Objective_ID)
      references View (Objective_ID) on delete restrict on update restrict;

alter table Prerequisites add constraint FK_T_Prerequisites_2_Attack_Patterns_1 foreign key (Prerequisites_ID)
      references Attack_Pattern (Prerequisites_ID) on delete restrict on update restrict;

alter table Previous_Entry_Name add constraint FK_T_Previous_Entry_Name_2_Previous_Entry_Names_1 foreign key (ID)
      references Previous_Entry_Names (Previous_Entry_Name_ID) on delete restrict on update restrict;

alter table Previous_Entry_Names add constraint FK_T_Previous_Entry_Names_2_Content_History_1 foreign key (Previous_Entry_Names_ID)
      references Content_History (Previous_Entry_Names_ID) on delete restrict on update restrict;

alter table Reference add constraint FK_T_Reference_2_References_1 foreign key (ID)
      references ReferencesT (Reference_ID) on delete restrict on update restrict;

alter table ReferencesT add constraint FK_T_References_2_Category_1 foreign key (References_ID)
      references Category (References_ID) on delete restrict on update restrict;

alter table ReferencesT add constraint FK_T_References_2_View_References_1 foreign key (References_ID)
      references View (References_ID) on delete restrict on update restrict;

alter table Related_Attack_Pattern add constraint FK_T_Related_Attack_Pattern_2_Related_Attack_Patterns_1 foreign key (ID)
      references Related_Attack_Patterns (Related_Attack_Pattern_ID) on delete restrict on update restrict;

alter table Related_Attack_Patterns add constraint FK_T_Related_Attack_Patterns_2_Attack_Pattern_1 foreign key (Related_Attack_Patterns_ID)
      references Attack_Pattern (Related_Attack_Patterns_ID) on delete restrict on update restrict;

alter table Related_Weakness add constraint FK_T_Related_Weakness_2_Related_Weaknesses_1 foreign key (ID)
      references Related_Weaknesses (Related_Weakness_ID) on delete restrict on update restrict;

alter table Related_Weaknesses add constraint FK_T_Related_Weaknesses_2_Attack_Pattern_1 foreign key (Related_Weaknesses_ID)
      references Attack_Pattern (Related_Weaknesses_ID) on delete restrict on update restrict;

alter table Relationships add constraint FK_T_Relationships_2_Category_1 foreign key (Relationships_ID)
      references Category (Relationships_ID) on delete restrict on update restrict;

alter table Resources_Required add constraint FK_T_Resources_Required_2_Attack_Pattern_1 foreign key (Resources_Required_ID)
      references Attack_Pattern (Resources_Required_ID) on delete restrict on update restrict;

alter table Scopes add constraint FK_T_Scopes_2_Consequnece_1 foreign key (Scopes_ID)
      references Consequence (Scopes_ID) on delete restrict on update restrict;

alter table Skills_Required add constraint FK_T_Skills_Required_2_Attack_Pattern_1 foreign key (Skills_Required_ID)
      references Attack_Pattern (Skills_Required_ID) on delete restrict on update restrict;

alter table Submission add constraint FK_T_Submission_2_Content_History_1 foreign key (ID)
      references Content_History (Submission_ID) on delete restrict on update restrict;

alter table Taxonomy_Mapping add constraint FK_T_Taxonomy_Mapping_2_Taxonomy_Mappings_1 foreign key (ID)
      references Taxonomy_Mappings (Taxonomy_Mapping_ID) on delete restrict on update restrict;

alter table Taxonomy_Mappings add constraint FK_T_Taxonomy_Mappings_2_Attack_Pattern_1 foreign key (Taxonomy_Mappings_ID)
      references Attack_Pattern (Taxonomy_Mappings_ID) on delete restrict on update restrict;

alter table Taxonomy_Mappings add constraint FK_T_Taxonomy_Mappings_2_Category_1 foreign key (Taxonomy_Mappings_ID)
      references Category (Taxonomy_Mappings_ID) on delete restrict on update restrict;

alter table Techniques add constraint FK_T_Techniques_2_Attack_Steps_1 foreign key (Techniques_ID)
      references Attack_Steps (Techniques_ID) on delete restrict on update restrict;

alter table View add constraint FK_T_View_2_Attack_Pattern_Catalog_1 foreign key (View_ID)
      references Attack_Pattern_Catalog (Views_ID) on delete restrict on update restrict;

