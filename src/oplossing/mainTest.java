package oplossing;

import opgave.SearchTree;
import opgave.samplers.Sampler;
import opgave.samplers.ZipfSampler;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
//deze klasse bestaat puur voor mijn eigen testen en een aantal benchmarks
public class mainTest {


    public static void main(String[] args) throws IOException {
        BufferedWriter addWriter = new BufferedWriter(new FileWriter("extra/addVsAdd2.csv"));
        BufferedWriter removeWriter = new BufferedWriter(new FileWriter("extra/TopVsBottomRemoveNormal.csv"));
        BufferedWriter addZipfWriter = new BufferedWriter(new FileWriter("extra/addVsAdd2Zipf.csv"));
        BufferedWriter removeZipfWriter = new BufferedWriter(new FileWriter("extra/TopVsBottomRemoveZipf.csv"));
        BufferedWriter searchWriter = new BufferedWriter(new FileWriter("extra/normalSearch.csv"));
        BufferedWriter searchZipfWriter = new BufferedWriter(new FileWriter("extra/zipSearch.csv"));
        Random rand = new Random();


        BottomUpSemiSplayTwoThreeTree<Integer> bottomUpTree = new BottomUpSemiSplayTwoThreeTree<>();
        SearchTree<Integer> topDownTree = new TopDownSemiSplayTwoThreeTree<>();
        SearchTree<Integer> twoThreeTree = new TwoThreeTree<>();
        int size = 500;
        int testsize = 200;

        for (int i = 0; i < testsize; i++) {
            searchWriter.append(String.valueOf(size + i * 500)).append(",");
            searchZipfWriter.append(String.valueOf(size + i * 500)).append(",");
            addWriter.append(String.valueOf(size + i * 500)).append(",");
            removeWriter.append(String.valueOf(size + i * 500)).append(",");
            addZipfWriter.append(String.valueOf(size + i * 500)).append(",");
            removeZipfWriter.append(String.valueOf(size + i * 500)).append(",");
        }

        addWriter.append("\n");
        removeWriter.append("\n");
        addZipfWriter.append("\n");
        removeZipfWriter.append("\n");
        searchWriter.append("\n");
        searchZipfWriter.append("\n");

        for (int i = 0; i < testsize; i++) {
            Sampler sampler = new Sampler(rand, size);
            ZipfSampler zipfSampler = new ZipfSampler(rand, size);

            List<Integer> list = sampler.sample(size);
            List<Integer> zipfList = zipfSampler.sample(size);
            long startAddNormal = System.currentTimeMillis();
            for (Integer el : list) {
                bottomUpTree.add(el);
            }

            addWriter.append(String.valueOf(System.currentTimeMillis() - startAddNormal)).append(",");
//
            long startSearchNormal = System.currentTimeMillis();
            for (Integer el : list) {
                for(int k=0 ; k<100 ; k++) {
                    bottomUpTree.contains(el);
                }
            }
            searchWriter.append(String.valueOf(System.currentTimeMillis() - startSearchNormal)).append(",");

            long startRemoveNormal = System.currentTimeMillis();
            for (Integer el : list) {
                bottomUpTree.remove(el);
            }
            removeWriter.append(String.valueOf(System.currentTimeMillis() - startRemoveNormal)).append(",");
//
            bottomUpTree.clear();
            long startAddZipf = System.currentTimeMillis();
            for (Integer el : zipfList) {
                bottomUpTree.add(el);
            }
            addZipfWriter.append(String.valueOf(System.currentTimeMillis() - startAddZipf)).append(",");

            long startSearchZipf = System.currentTimeMillis();
            for (Integer el : zipfList) {
                for(int k=0 ; k<100 ; k++) {
                    bottomUpTree.contains(el);
                }
            }
            searchZipfWriter.append(String.valueOf(System.currentTimeMillis() - startSearchZipf)).append(",");

            long startRemoveZipf = System.currentTimeMillis();
            for (Integer el : zipfList) {
                bottomUpTree.remove(el);
            }
            removeZipfWriter.append(String.valueOf(System.currentTimeMillis() - startRemoveZipf)).append(",");


            size += 500;
//            System.out.println("jep");

        }

        addWriter.append("\n");
        removeWriter.append("\n");
        addZipfWriter.append("\n");
        removeZipfWriter.append("\n");
        searchWriter.append("\n");
        searchZipfWriter.append("\n");
        size = 500;
        for (int i = 0; i < testsize; i++) {
            Sampler sampler = new Sampler(rand, size);
            ZipfSampler zipfSampler = new ZipfSampler(rand, size);

            List<Integer> list = sampler.sample(size);
            List<Integer> zipfList = zipfSampler.sample(size);
            long startAddNormal = System.currentTimeMillis();
            for (Integer el : list) {
                topDownTree.add(el);
                assertTrue(topDownTree.contains(el));
            }
            addWriter.append(String.valueOf(System.currentTimeMillis() - startAddNormal)).append(",");

            addWriter.append(String.valueOf(System.currentTimeMillis() - startAddNormal)).append(",");
            long startSearchNormal = System.currentTimeMillis();
            for (Integer el : list) {
                for(int k=0 ; k<100 ; k++) {
                    topDownTree.contains(el);
                }
            }
            searchWriter.append(String.valueOf(System.currentTimeMillis() - startSearchNormal)).append(",");

            long startRemoveNormal = System.currentTimeMillis();
            for (Integer el : list) {
                topDownTree.remove(el);
            }
            removeWriter.append(String.valueOf(System.currentTimeMillis() - startRemoveNormal)).append(",");
//
            topDownTree.clear();
            long startAddZipf = System.currentTimeMillis();
            for (Integer el : zipfList) {
                topDownTree.add(el);
            }
            addZipfWriter.append(String.valueOf(System.currentTimeMillis() - startAddZipf)).append(",");

            long startSearchZipf = System.currentTimeMillis();
            for (Integer el : zipfList) {
                for(int k=0 ; k<100 ; k++) {
                    topDownTree.contains(el);
                }
            }
            searchZipfWriter.append(String.valueOf(System.currentTimeMillis() - startSearchZipf)).append(",");

            long startRemoveZipf = System.currentTimeMillis();
            for (Integer el : zipfList) {
                topDownTree.remove(el);
            }
            removeZipfWriter.append(String.valueOf(System.currentTimeMillis() - startRemoveZipf)).append(",");
            size += 500;
        }

        addWriter.append("\n");
        removeWriter.append("\n");
        addZipfWriter.append("\n");
        removeZipfWriter.append("\n");
        searchWriter.append("\n");
        searchZipfWriter.append("\n");
        size = 500;
        for (int i = 0; i < testsize; i++) {
            Sampler sampler = new Sampler(rand, size);
            ZipfSampler zipfSampler = new ZipfSampler(rand, size);

            List<Integer> list = sampler.sample(size);
            List<Integer> zipfList = zipfSampler.sample(size);
            long startAddNormal = System.currentTimeMillis();
            for (Integer el : list) {
                twoThreeTree.add(el);
            }
            addWriter.append(String.valueOf(System.currentTimeMillis() - startAddNormal)).append(",");

            addWriter.append(String.valueOf(System.currentTimeMillis() - startAddNormal)).append(",");
            long startSearchNormal = System.currentTimeMillis();
            for (Integer el : list) {
                for(int k=0 ; k<100 ; k++) {
                    twoThreeTree.contains(el);
                }
            }
            searchWriter.append(String.valueOf(System.currentTimeMillis() - startSearchNormal)).append(",");

            long startRemoveNormal = System.currentTimeMillis();
            for (Integer el : list) {
                twoThreeTree.remove(el);
            }
            removeWriter.append(String.valueOf(System.currentTimeMillis() - startRemoveNormal)).append(",");
//
            twoThreeTree.clear();
            long startAddZipf = System.currentTimeMillis();
            for (Integer el : zipfList) {
                twoThreeTree.add(el);
            }
            addZipfWriter.append(String.valueOf(System.currentTimeMillis() - startAddZipf)).append(",");

            long startSearchZipf = System.currentTimeMillis();
            for (Integer el : zipfList) {
                for(int k=0 ; k<100 ; k++) {
                    twoThreeTree.contains(el);
                }
            }
            searchZipfWriter.append(String.valueOf(System.currentTimeMillis() - startSearchZipf)).append(",");

            long startRemoveZipf = System.currentTimeMillis();
            for (Integer el : zipfList) {
                twoThreeTree.remove(el);
            }
            removeZipfWriter.append(String.valueOf(System.currentTimeMillis() - startRemoveZipf)).append(",");
            size += 500;
        }

        addWriter.close();
        removeWriter.close();
        addZipfWriter.close();
        removeZipfWriter.close();
        searchWriter.close();
        searchZipfWriter.close();
    }
}


//    public static void main(String[] args) {
//        SearchTree<Integer> tree = new TopDownSemiSplayTwoThreeTree<>();
//        Sampler sampler = new Sampler(new Random(),1000);
//        Sampler removeSampler = new Sampler(new Random(),1000);
//        List<Integer> list = sampler.getElements();
////        ArrayList<Integer> list = new ArrayList<>();
////        list.add(3);
////        list.add(0);
////        list.add(1);
////        list.add(9);
////        list.add(5);
////        list.add(8);
////        list.add(2);
////        list.add(6);
////        list.add(7);
////        list.add(4);
////        list.add(79);
////        list.add(48);
////        list.add(44);
////        list.add(56);
////        list.add(87);
////        list.add(81);
////        list.add(32);
////        list.add(97);
////        list.add(15);
////        list.add(82);
////        list.add(83);
////        list.add(84);
////        list.add(62);
////        list.add(71);
////        list.add(64);
////        list.add(98);
////        list.add(80);
////        list.add(22);
////        list.add(5);
////        list.add(9);
////        list.add(54);
////        list.add(75);
////        list.add(70);
////        list.add(39);
////        list.add(73);
////        list.add(57);
////        list.add(10);
////        list.add(37);
////        list.add(6);
////        list.add(68);
////        list.add(2);
////        list.add(51);
////        list.add(58);
////        list.add(61);
////        list.add(85);
////        list.add(27);
////        list.add(11);
////        list.add(13);
////        list.add(49);
////        list.add(74);
////        list.add(76);
////        list.add(20);
////        list.add(55);
////        list.add(28);
////        list.add(66);
////        list.add(1);
////        list.add(33);
////        list.add(60);
////        list.add(94);
////        list.add(41);
////        list.add(21);
////        list.add(30);
////        list.add(96);
////        list.add(29);
////        list.add(86);
////        list.add(17);
////        list.add(88);
////        list.add(4);
////        list.add(3);
////        list.add(89);
////        list.add(45);
////        list.add(93);
////        list.add(23);
////        list.add(53);
////        list.add(8);
////        list.add(36);
////        list.add(16);
////        list.add(35);
////        list.add(0);
////        list.add(43);
////        list.add(25);
////        list.add(67);
////        list.add(18);
////        list.add(95);
////        list.add(50);
////        list.add(40);
////        list.add(90);
////        list.add(77);
////        list.add(69);
////        list.add(46);
////        list.add(26);
////        list.add(63);
////        list.add(91);
////        list.add(7);
////        list.add(14);
////        list.add(42);
////        list.add(47);
////        list.add(34);
////        list.add(19);
////        list.add(24);
//
//
//        for (int i = 0; i < list.size(); i++) {
//            if (i == 9) {
//                System.out.println("hey");
//            }
//            assertTrue(tree.add(list.get(i)));
////            for(int k=0 ; k<=i ; k++){
////                if(!tree.contains(list.get(k))){
////                    System.out.println(list.get(i));
////                    System.out.println(list.get(k));
////                }
////            }
//        }
////        for (int l : list) {
////            assertTrue(tree.contains(l));
////        }
//////
//        for (int i = 0; i < list.size(); i++) {
////            if(i == 46){
////                System.out.println("hey");
////            }
//            assertTrue(tree.remove(list.get(i)));
////            for (int k = i + 1; k < list.size(); k++) {
////                if (!tree.contains(list.get(k))) {
////                    System.out.println(list.get(i));
////                    System.out.println(list.get(k));
////                }
////            }
//            assertFalse(tree.contains(list.get(i)));
//        }
////        addRemoveRandomElements();
//    }
//}
//
//    public static void removeTest() {
//        SearchTree<Integer> tree = new TwoThreeTree<>();
//
//        assertFalse(tree.contains(1));
//        tree.add(1);
//        assertTrue(tree.contains(1));
//        tree.remove(1);
//        assertFalse(tree.contains(1));
//        assertEquals(0, tree.size());
//        assertTrue(tree.isEmpty());
//    }
//
//    public static void addOne() {
//        SearchTree<Integer>tree = new BottomUpSemiSplayTwoThreeTree<>();
//
//        assertFalse(tree.contains(1));
//        tree.add(1);
//        assertTrue(tree.contains(1));
//        assertEquals(1, tree.size());
//    }
//
//
//    public static void removeMultiple() {
//        SearchTree<Integer>tree = new BottomUpSemiSplayTwoThreeTree<>();
//
//        for (int i = 0; i < 10; i++) {
//            assertTrue(tree.add(i), String.format("should change when adding %d", i));
//        }
//        for (int i = 0; i < 10; i++) {
//            assertTrue(tree.contains(i), String.format("should contain %d", i));
//            assertTrue(tree.remove(i), String.format("should change when removing %d", i));
//            assertFalse(tree.contains(i), String.format("should not contain %d anymore", i));
//        }
//        assertEquals(0, tree.size(), "should be empty");
//    }
//
//    public static void addRemoveRandomElements(){
//        SearchTree<Integer> tree = new TopDownSemiSplayTwoThreeTree<>();
//        Sampler sampler = new Sampler(new Random(),15);
//        Sampler removeSampler = new Sampler(new Random(),15);
//        List<Integer> list = sampler.getElements();
////        for(int l: list){
////            assertTrue(tree.add(l));
////        }
//
//        for(int i=0 ; i<list.size() ; i++) {
//            if(list.get(i) == 16){
//                System.out.println("hey");
//            }
//            assertTrue(tree.add(list.get(i)));
////            for(int k=0 ; k<=i ; k++){
////                if(!tree.contains(list.get(k))){
////                    System.out.println(list.get(i));
////                    System.out.println(list.get(k));
////                }
////            }
//        }
//        for(int l:list){
//            assertTrue(tree.contains(l));
//        }
//    }
//}
