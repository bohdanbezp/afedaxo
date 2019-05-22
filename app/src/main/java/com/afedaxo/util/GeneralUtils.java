package com.afedaxo.util;

import android.util.SparseArray;
import android.view.View;
import android.widget.RadioGroup;

import java.io.ByteArrayOutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

public class GeneralUtils {
    public static long bytesToLong(byte[] b) {
        return ((long) b[7] << 56)
                | ((long) b[6] & 0xff) << 48
                | ((long) b[5] & 0xff) << 40
                | ((long) b[4] & 0xff) << 32
                | ((long) b[3] & 0xff) << 24
                | ((long) b[2] & 0xff) << 16
                | ((long) b[1] & 0xff) << 8
                | ((long) b[0] & 0xff);
    }

    public static byte[] rand(byte[] seed, int n) {
        try {
            byte[] data = null;
            ByteArrayOutputStream ret = new ByteArrayOutputStream(n);
            while (ret.size() < n) {
                MessageDigest md = MessageDigest.getInstance("SHA1");
                md.update(seed);
                if (data != null)
                    md.update(data);
                data = md.digest();
                ret.write(data, 0, Math.min(n - ret.size(), data.length));
            }
            return ret.toByteArray();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public static <C> List<C> asList(SparseArray<C> sparseArray) {
        if (sparseArray == null) return null;
        List<C> arrayList = new ArrayList<C>(sparseArray.size());
        for (int i = 0; i < sparseArray.size(); i++)
            arrayList.add(sparseArray.valueAt(i));
        return arrayList;
    }

    public static int getRadioGroupId(RadioGroup radioButtonGroup) {
        int radioButtonID = radioButtonGroup.getCheckedRadioButtonId();
        View radioButton = radioButtonGroup.findViewById(radioButtonID);
        int idx = radioButtonGroup.indexOfChild(radioButton);
        return idx;
    }


}
