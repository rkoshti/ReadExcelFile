package com.readexcel;

import android.app.Activity;
import android.content.ContentProviderOperation;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;

public class MainActivity extends Activity implements View.OnClickListener {

    private Button btnReadXLS;
    private Button btnReadXLSX;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        //setSupportActionBar(toolbar);

        btnReadXLS = (Button) findViewById(R.id.btnReadXLS);
        btnReadXLSX = (Button) findViewById(R.id.btnReadXLSX);

        btnReadXLS.setOnClickListener(this);
        btnReadXLSX.setOnClickListener(this);


    }


    public void readXLSFileFromAssets() {


        try {
            // Creating Input Stream

            InputStream myInput;

            //  Don't forget to Change to your assets folder excel sheet
            myInput = getAssets().open("contact.xls");

            // Create a POIFSFileSystem object
            POIFSFileSystem myFileSystem = new POIFSFileSystem(myInput);

            // Create a workbook using the File System
            HSSFWorkbook myWorkBook = new HSSFWorkbook(myFileSystem);

            // Get the first sheet from workbook
            HSSFSheet mySheet = myWorkBook.getSheetAt(0);

            /** We now need something to iterate through the cells. **/
            Iterator<Row> rowIter = mySheet.rowIterator();

            ArrayList<ContactBean> contactList = new ArrayList<>();

            while (rowIter.hasNext()) {
                HSSFRow myRow = (HSSFRow) rowIter.next();
                Iterator<Cell> cellIter = myRow.cellIterator();

                ContactBean bean = new ContactBean();

                while (cellIter.hasNext()) {

                    HSSFCell myCell = (HSSFCell) cellIter.next();
                    if (myCell.getColumnIndex() == 0) {

                        bean.setFirstName(myCell.toString());
                    }

                    if (myCell.getColumnIndex() == 1) {

                        bean.setMobile(myCell.toString());
                    }

                    Log.e("FileUtils", "Cell Value: " + myCell.toString() + " Index :" + myCell.getColumnIndex());

                }

                contactList.add(bean);
            }

            // contactList.size();

            //  AddNewContact(contactList);

        } catch (Exception e) {
            //e.printStackTrace();
        }

        return;
    }

    public void readXLSXFileFromAssets() {

        try {
            // Creating Input Stream

            InputStream myInput;

            //  Don't forget to Change to your assets folder excel sheet
            myInput = getAssets().open("contact_new.xlsx");

            XSSFWorkbook workbook = new XSSFWorkbook(myInput);
            XSSFSheet mySheet = workbook.getSheetAt(0);


            /** We now need something to iterate through the cells. **/
            Iterator<Row> rowIter = mySheet.rowIterator();

            ArrayList<ContactBean> contactList = new ArrayList<>();

            while (rowIter.hasNext()) {
                Row myRow = (Row) rowIter.next();
                Iterator<Cell> cellIter = myRow.cellIterator();

                ContactBean bean = new ContactBean();

                while (cellIter.hasNext()) {

                    XSSFCell myCell = (XSSFCell) cellIter.next();
                    if (myCell.getColumnIndex() == 0) {

                        bean.setFirstName(myCell.toString());
                    }

                    if (myCell.getColumnIndex() == 1) {

                        bean.setMobile(myCell.toString());
                    }

                    Log.e("FileUtils", "Cell Value: " + myCell.toString() + " Index :" + myCell.getColumnIndex());

                }

                contactList.add(bean);
            }

            // contactList.size();

            //  AddNewContact(contactList);

        } catch (Exception e) {
            // e.printStackTrace();
        }

        return;
    }

    private void AddNewContact(ArrayList<ContactBean> contactList) {

        String DisplayName = contactList.get(5).getFirstName();
        String MobileNumber = contactList.get(5).getMobile();
        String HomeNumber = "1111";
        String WorkNumber = "2222";
        String emailID = "email@nomail.com";
        String company = "bad";
        String jobTitle = "abcd";

        ArrayList<ContentProviderOperation> ops = new ArrayList<>();

        ops.add(ContentProviderOperation.newInsert(
                ContactsContract.RawContacts.CONTENT_URI)
                .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null)
                .build());

        //------------------------------------------------------ Names
        if (DisplayName != null) {
            ops.add(ContentProviderOperation.newInsert(
                    ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                    .withValue(ContactsContract.Data.MIMETYPE,
                            ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                    .withValue(
                            ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME,
                            DisplayName).build());
        }

        //------------------------------------------------------ Mobile Number
        if (MobileNumber != null) {
            ops.add(ContentProviderOperation.
                    newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                    .withValue(ContactsContract.Data.MIMETYPE,
                            ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, MobileNumber)
                    .withValue(ContactsContract.CommonDataKinds.Phone.TYPE,
                            ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE)
                    .build());
        }

        //------------------------------------------------------ Home Numbers
        if (HomeNumber != null) {
            ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                    .withValue(ContactsContract.Data.MIMETYPE,
                            ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, HomeNumber)
                    .withValue(ContactsContract.CommonDataKinds.Phone.TYPE,
                            ContactsContract.CommonDataKinds.Phone.TYPE_HOME)
                    .build());
        }

        //------------------------------------------------------ Work Numbers
        if (WorkNumber != null) {
            ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                    .withValue(ContactsContract.Data.MIMETYPE,
                            ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, WorkNumber)
                    .withValue(ContactsContract.CommonDataKinds.Phone.TYPE,
                            ContactsContract.CommonDataKinds.Phone.TYPE_WORK)
                    .build());
        }

        //------------------------------------------------------ Email
        if (emailID != null) {
            ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                    .withValue(ContactsContract.Data.MIMETYPE,
                            ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.Email.DATA, emailID)
                    .withValue(ContactsContract.CommonDataKinds.Email.TYPE, ContactsContract.CommonDataKinds.Email.TYPE_WORK)
                    .build());
        }

        //------------------------------------------------------ Organization
        if (!company.equals("") && !jobTitle.equals("")) {
            ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                    .withValue(ContactsContract.Data.MIMETYPE,
                            ContactsContract.CommonDataKinds.Organization.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.Organization.COMPANY, company)
                    .withValue(ContactsContract.CommonDataKinds.Organization.TYPE, ContactsContract.CommonDataKinds.Organization.TYPE_WORK)
                    .withValue(ContactsContract.CommonDataKinds.Organization.TITLE, jobTitle)
                    .withValue(ContactsContract.CommonDataKinds.Organization.TYPE, ContactsContract.CommonDataKinds.Organization.TYPE_WORK)
                    .build());
        }

        // Asking the Contact provider to create a new contact
        try {
            getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(MainActivity.this, "Exception: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.btnReadXLS:


                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        readXLSFileFromAssets();
                    }
                }).start();


                break;

            case R.id.btnReadXLSX:


                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        readXLSXFileFromAssets();
                    }
                }).start();


                break;
        }

    }

}
