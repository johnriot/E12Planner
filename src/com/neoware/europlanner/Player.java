package com.neoware.europlanner;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.Locale;

import junit.framework.Assert;

public class Player {

    private final String mPos;
    private final String mNumber;
    private final String mName;
    private Date mDateOfBirth;


    public Player(String pos, String number, String name, String dob) {
        mPos = pos;
        mNumber = number;
        mName = name;
        DateFormat format =
                DateFormat.getDateInstance(DateFormat.LONG, Locale.US);
        try {
            mDateOfBirth = format.parse(dob);
        } catch(ParseException pe) {
            Assert.fail(pe.getMessage());
        }
    }

    public String getPos() {
        return mPos;
    }

    public String getPositionFilename() throws PlayerPositionException {
        if(mPos.equals("G")) {
            return "goalkeeper";
        }
        else if(mPos.equals("D")) {
            return "defender";
        }
        else if(mPos.equals("M")) {
            return "midfielder";
        }
        else if(mPos.equals("F")) {
            return "forward";
        }
        else {
            throw new PlayerPositionException();
        }
    }

    public String getNumber() {
        return mNumber;
    }

    public String getName() {
        return mName;
    }

    public int getAge() {
        Date today = new Date();
        int years = today.getYear() - mDateOfBirth.getYear() - 1;
        if(today.getMonth() > mDateOfBirth.getMonth()) {
            years += 1;
        }
        else if((today.getMonth() == mDateOfBirth.getMonth()) &&
                today.getDate() >= mDateOfBirth.getDate()) {
            years += 1;
        }
        return years;
    }

    class PlayerPositionException extends Exception {
        private static final long serialVersionUID = -7748157905927939921L;
    }
}
