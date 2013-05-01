import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Anang Ferdi Kusuma (1006665952)
 * @author Arief Yudha Satria (1006665984)
 * @author Evan Ariansyah H (1006671381)
 */
public class XTSAES {

    AES aes;
    File i;
    File k;
    File o;
    private static byte[] tweak = Util.hex2byte("0123456789abcdef0123456789abcdef");
    byte[] key1;
    byte[] key2;
    byte[][] tTable;
    byte[] plain;
    byte[][] plainBlock;
    byte[] tweakAfterEncrypt;

    public XTSAES(File input, File key, File output, int mode) {
        aes = new AES();
        i = input;
        k = key;
        o = output;
    }

    public void start() {
        try {
            System.out.println(0x100);
            //key
            FileReader fr = new FileReader(k);
            BufferedReader buff = new BufferedReader(fr);
            String temp = buff.readLine();
            key1 = temp.substring(0, 16).getBytes();
            key2 = temp.substring(16, 32).getBytes();
            aes.setKey(key2);
            String input = "";
            //input
            fr = new FileReader(i);
            buff = new BufferedReader(fr);
            temp = buff.readLine();
            if (temp != null) {
                input = temp;
            }
            while ((temp = buff.readLine()) != null) {
                input = input + "\r" + temp;
            }
            System.out.println(input);
            plain = input.getBytes();
            plainBlock = new byte[(int) Math.ceil(plain.length / 16)][16];
            for (int i = 0; i < plainBlock.length; i++) {
                System.arraycopy(plain, 16 * i, plainBlock[i], 0, 16);
            }
            for (int i = 0; i < plain.length; i++) {
                System.out.print(plain[i] + " ");
            }
            System.out.println();
            for (int i = 0; i < plainBlock.length; i++) {
                for (int j = 0; j < plainBlock[i].length; j++) {
                    System.out.print(plainBlock[i][j] + " ");
                }
                System.out.println();
            }
            tTable = new byte[plainBlock.length][16];
            tweakAfterEncrypt = aes.encrypt(tweak);
            fillTTable();
            //output
            FileWriter fw = new FileWriter(o);
            BufferedWriter bw = new BufferedWriter(fw);
            for (int i = 0; i < plainBlock.length - 2; i++) {
                xtsBlockEncrypt(plain, i);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public byte[] xtsBlockEncrypt(byte[] plain, int j) {
        byte[] pp = xor(plain, tTable[j]);
        aes.setKey(key1);
        byte[] cc = aes.encrypt(pp);
        byte[] cj = xor(cc, tTable[j]);
        return cj;
    }

    public byte[] xtsBlockDecrypt(byte[] cipher, int j) {
        byte[] cc = xor(cipher, tTable[j]);
        aes.setKey(key1);
        byte[] pp = aes.decrypt(cc);
        byte[] pj = xor(pp, tTable[j]);
        return pj;
    }

    private void fillTTable() {
        tTable[0] = new byte[16];
        tTable[0] = tweakAfterEncrypt;
        for (int i = 1; i < plain.length; i++) {
            byte[] temp = new byte[16];
            temp[0] = (byte) ((tTable[i - 1][0] << 1) % 128
                    ^ 135 * (tTable[i - 1][15] >>> 7));
            for (int j = 1; j < 16; j++) {
                temp[j] = (byte) ((tTable[i - 1][j] << 1) % 128
                        ^ (tTable[i - 1][j - 1] >>> 7));
            }
            tTable[i] = temp;
        }
    }

    private byte[] xor(byte[] a, byte[] b) {
        byte[] res = new byte[a.length];
        for (int i = 0; i < res.length; i++) {
            res[i] = (byte) (a[i] ^ b[i]);
        }
        return res;
    }
}
