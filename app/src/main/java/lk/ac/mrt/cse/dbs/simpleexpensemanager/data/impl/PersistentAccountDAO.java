package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.DatabaseHelper;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;

public class PersistentAccountDAO implements AccountDAO {

    private Context context ;

    public PersistentAccountDAO(Context context){
        this.context=context;
    }
    public List<String> getAccountNumbersList() {
        DatabaseHelper dbHelper =new DatabaseHelper(context);
        SQLiteDatabase  db = dbHelper.getReadableDatabase();
            return dbHelper.getAllAccountNo(db);
    }

    @Override
    public List<Account> getAccountsList() {
        DatabaseHelper dbHelper =new DatabaseHelper(context);

        SQLiteDatabase  db = dbHelper.getReadableDatabase();
        return dbHelper.getAllAccounts(db);
    }

    @Override
    public Account getAccount(String accountNo) throws InvalidAccountException {
        DatabaseHelper dbHelper =new DatabaseHelper(context);

        SQLiteDatabase  db = dbHelper.getReadableDatabase();
        return dbHelper.getAccount(accountNo,db);
    }

    @Override
    public void addAccount(Account account, Context context) {
        DatabaseHelper dbHelper =new DatabaseHelper(context);
        SQLiteDatabase  db = dbHelper.getWritableDatabase();
        dbHelper.insertAccount(account,db);
    }

    @Override
    public void removeAccount(String accountNo) throws InvalidAccountException {
        DatabaseHelper dbHelper =new DatabaseHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(DatabaseHelper.TABLE_ACCOUNT, DatabaseHelper.KEY_ID + " = ?",
                new String[] { accountNo });
        db.close();

    }

    @Override
    public void updateBalance(String accountNo, ExpenseType expenseType, double amount) throws InvalidAccountException {

        Account account = this.getAccount(accountNo);
        DatabaseHelper dbHelper =new DatabaseHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        switch (expenseType) {
            case EXPENSE:
                values.put(DatabaseHelper.KEY_INI_BAL, account.getBalance() - amount);
                break;
            case INCOME:
                values.put(DatabaseHelper.KEY_INI_BAL, account.getBalance() + amount);
                break;
        }
        // updating row
        db.update(DatabaseHelper.TABLE_ACCOUNT, values, DatabaseHelper.KEY_ID + " = ?",
                new String[] { accountNo });
    }

}
}
