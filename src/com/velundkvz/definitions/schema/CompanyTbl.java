package com.velundkvz.definitions.schema;

public final class CompanyTbl
{
   public static final String COL_ID = "id";
   public static final String COL_NAME = "name";
   public static final String COL_E_MAIL = "email";
   public static final String COL_PASSWORD = "password";

   private CompanyTbl()
   {
      throw new AssertionError("constants container not for implementing");
   }
}
