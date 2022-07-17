package com.velundkvz.definitions.schema;

import static com.velundkvz.definitions.schema.DBSchema.COMPANY_TBL;

public final class CompanyTbl
{
   public static final String COL_ID = "id";
   public static final String COL_NAME = "name";
   public static final String COL_E_MAIL_COMPANY_TBL = "email";
   public static final String COL_PASSWORD = "password";

   public static String INSERT_COMPANY_SQL = "insert into " + COMPANY_TBL + " values (null, ?,?,?)";
   public static final String DELETE_COMPANY_SQL = "delete from " + COMPANY_TBL + " where " + COL_ID +  " = ?";
   public static final String UPDATE_COMPANY_EMAIL_BY_ID_SQL = "update " + COMPANY_TBL + " set " + COL_E_MAIL_COMPANY_TBL + " = ?"   + " where " + COL_ID + " = " + "?" ;
   public static final String SELECT_COMPANY_BY_ID_SQL = "select * from " + COMPANY_TBL + " where " + COL_ID + " = " + "?";
   public static final String SELECT_ALL_COMPANIES_SQL = "select * from " + COMPANY_TBL;
   public static final String SELECT_COMPANY_BY_EMAIL_AND_PSWD_SQL = "select * from " + COMPANY_TBL + " where " + COL_E_MAIL_COMPANY_TBL + " = ?" + " and " + COL_PASSWORD + " = ?";
   public static final String COUNT_COMPANY_SQL = "select count(*) from " + COMPANY_TBL;
   public static final String GET_COMPANY_MAX_ID_SQL = "select max(" + COL_ID + ")" + " from " +  COMPANY_TBL;
   public static final String IS_COMPANY_EXISTS_BY_ID_SQL = "select exists (select " + COL_ID + " from " + COMPANY_TBL + " where " + COL_ID + " = ?)";
   public static final String IS_COMPANY_EXISTS_BY_EMAIL_SQL = "select exists (select " + COL_E_MAIL_COMPANY_TBL + " from " + COMPANY_TBL + " where " + COL_E_MAIL_COMPANY_TBL + " = ?)";


   private CompanyTbl()
   {
      throw new AssertionError("constants container not for implementing");
   }
}
