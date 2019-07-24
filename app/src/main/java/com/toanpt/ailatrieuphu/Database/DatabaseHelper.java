package com.toanpt.ailatrieuphu.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;

import com.toanpt.ailatrieuphu.Model.HighScore;
import com.toanpt.ailatrieuphu.Model.Question;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper {

    private static final String TABLE_NAME = "Question";
    private static final String COL_ID = "_id";
    private static final String COL_QUESTION = "question";
    private static final String COL_CASE_A = "casea";
    private static final String COL_CASE_B = "caseb";
    private static final String COL_CASE_C = "casec";
    private static final String COL_CASE_D = "cased";
    private static final String COL_TRUE_CASE = "truecase";
    private static final String COL_LEVEL = "level";
    private static String DB_NAME = "Question.sqlite";
    private String DB_PATH = Environment.getDataDirectory() + "/data/com.toanpt.ailatrieuphu/databases/" + DB_NAME;
    private Context context;
    public SQLiteDatabase db;


    public DatabaseHelper(Context context) {
        this.context = context;
        copyDataBase(context);
    }

    private void copyDataBase(Context context) {
        File file = new File(DB_PATH);
        if (file.exists()) {
            return;
        }
        File parent = file.getParentFile();
        parent.mkdirs();
        try {
            FileOutputStream outputStream = new FileOutputStream(file);
            InputStream inputStream = context.getAssets().open(DB_NAME);
            byte[] bytes = new byte[1024];
            int count;
            while ((count = inputStream.read(bytes)) > 0) {
                outputStream.write(bytes, 0, count);
            }
            outputStream.flush();
            outputStream.close();
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Question getQuestion(int dokho) {
        db = context.openOrCreateDatabase(DB_NAME, context.MODE_PRIVATE, null);

        String select = "SELECT * FROM Question WHERE level = " + dokho + " ORDER BY random() LIMIT 1";
        Cursor cursor = db.rawQuery(select, null);
        cursor.moveToFirst();
        int indexId = cursor.getColumnIndex(COL_ID);
        int indexQuestion = cursor.getColumnIndex(COL_QUESTION);
        int indexCaseA = cursor.getColumnIndex(COL_CASE_A);
        int indexCaseB = cursor.getColumnIndex(COL_CASE_B);
        int indexCaseC = cursor.getColumnIndex(COL_CASE_C);
        int indexCaseD = cursor.getColumnIndex(COL_CASE_D);
        int indexTrueCase = cursor.getColumnIndex(COL_TRUE_CASE);
        int indexLevel = cursor.getColumnIndex(COL_LEVEL);

        cursor.moveToFirst();

        int id = cursor.getInt(indexId);
        String question = cursor.getString(indexQuestion);
        String caseA = cursor.getString(indexCaseA);
        String caseB = cursor.getString(indexCaseB);
        String caseC = cursor.getString(indexCaseC);
        String caseD = cursor.getString(indexCaseD);
        int trueCase = cursor.getInt(indexTrueCase);
        int level = cursor.getInt(indexLevel);

        Question cauhoi = new Question(id, question, caseA, caseB, caseC, caseD, trueCase, level);

        cursor.close();
        db.close();
        return cauhoi;
    }

    public void saveHighScore(String name, int highscore) {
        db = context.openOrCreateDatabase(DB_NAME, context.MODE_PRIVATE, null);

        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("highscore", highscore);

        db.insert("HighScore", null, contentValues);

        db.close();
    }

    public List<HighScore> getHighScore() {
        List<HighScore> list = new ArrayList<>();
        db = context.openOrCreateDatabase(DB_NAME, context.MODE_PRIVATE, null);

        String select = "SELECT * FROM HighScore ORDER BY highscore DESC LIMIT 15";
        Cursor cursor = db.rawQuery(select, null);
        cursor.moveToFirst();
        do {
            int idIndex = cursor.getColumnIndex("id");
            int nameIndex = cursor.getColumnIndex("name");
            int highscoreIndex = cursor.getColumnIndex("highscore");

            int id = cursor.getInt(idIndex);
            String name = cursor.getString(nameIndex);
            int highscore = cursor.getInt(highscoreIndex);

            HighScore highScore = new HighScore(id, name, highscore);
            list.add(highScore);
        } while (cursor.moveToNext());

        db.close();

        return list;
    }
}
