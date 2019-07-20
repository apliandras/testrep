package testrep;

import java.io.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class App {
    static List<String> penztar = new ArrayList<>();
    static Map kosarKezdeteIndex = new HashMap();
    static Map kosarVegeIndex = new HashMap();
    static List<String> kosar = new ArrayList(); //megadott vasarlas kosartartalma
    static Map kosarMap = new HashMap();

    public static void main(String[] args) throws IOException {
        feladat(1);
        readSourceFile();
        feladat(2);
        task2();
        feladat(3);
        task3();
        feladat(4);
        int sorszamVasarlas = Integer.parseInt(task4("Adja meg egy vasarlas sorszamat! "));
        String arucikkNev = task4("Adja meg egy arucikk nevet! ");
        int vasaroltDbSzam = Integer.parseInt(task4("Adja meg a vasarolt darabszamot! "));
        feladat(5);
        task5a(arucikkNev);
        task5b(arucikkNev);
        feladat(6);
        task6(vasaroltDbSzam);
        feladat(7);
        kosarIndexMapsFill();
        task7(sorszamVasarlas);
        sellDetails(sorszamVasarlas);
        feladat(8);
        /* writeToFile();*/
    }

    public static void feladat(int x) {
        System.out.println(" ");
        System.out.println(x + ". feladat");
    }

    public static void readSourceFile() throws IOException {    //beolvasas
        BufferedReader br = new BufferedReader(new FileReader("penztar.txt"));
        String row;
        while ((row = br.readLine()) != null) {
            penztar.add(row);
        }
        br.close();
    }

    public static void task2() {
        int fizetesekSzama = 0;
        for (String i : penztar) {
            if (i.equals("F"))
                fizetesekSzama++;
        }
        System.out.println("A fizetesek szama: " + fizetesekSzama);
    }

    public static void task3() {//elsö vasarlo kosara
        AtomicInteger product = new AtomicInteger();

        for (String i : penztar) {
            if (i.equals("F"))
                break;
            else
                product.getAndIncrement();
        }
        System.out.println("Az elsö vasarlo " + product + " darab arucikket vasarolt.");
    }

    public static String task4(String x) {
        System.out.print(x);
        Scanner sc = new Scanner(System.in);
        return sc.nextLine();
    }

    public static void task5a(String x) {
        int fCounter = 1;
        for (String i : penztar) {
            if (i.equals("F"))
                fCounter++;
            if (i.equals(x))
                break;
        }
        System.out.println("Az elsö vasarlas sorszama: " + fCounter);
        fCounter = 1;
        int xCounter = 0;
        for (String i : penztar) {
            if (i.equals("F"))
                fCounter++;
            if (i.equals(x))
                xCounter = fCounter;
        }
        System.out.println("Az utolso vasarlas sorszama: " + xCounter);
    }

    public static void task5b(String x) {
        int osszVasarls = 0;
        boolean vasaroltak = false;
        for (String i : penztar) {
            if (i.equals("F") && vasaroltak) {
                osszVasarls++;
                vasaroltak = false;
            }
            if (i.equals(x))
                vasaroltak = true;

        }
        System.out.println(osszVasarls + " vasarlas soran vettek belöle.");
    }

    public static void task6(int x) {
        System.out.println(x + " db vetelekor fizetendö: " + ertek(x));//fügveny beillesztve
    }

    public static int ertek(int x) { //a kert fügveny
        int a = 500;
        if (x == 1)
            a = 500;
        if (x == 2)
            a = 500 + 450;
        if (x > 2)
            a = 950 + (x - 2) * 400;
        return a;
    }

    //köztes methodok
    public static void kosarIndexMapsFill() {//feltöltöttem a kosarak indexet a penztar listböl
        kosarKezdeteIndex.put(1, 0);
        int kezdetCounter = 1;
        for (int i = 0; i < penztar.size(); i++) {
            if (penztar.get(i).equals("F")) {
                kezdetCounter++;
                kosarKezdeteIndex.put(kezdetCounter, i + 1);
            }
        }
        int vegeCounter = 0;
        for (int i = 0; i < penztar.size(); i++) {
            if (penztar.get(i).equals("F")) {
                vegeCounter++;
                kosarVegeIndex.put(vegeCounter, i - 1);
            }
        }
    }

    public static void kosarListFillById(int x) { //adott vasarlasi id hez betölti a kosarba a vasarlas tartalmat
        int min = (int) kosarKezdeteIndex.get(x);
        int max = (int) kosarVegeIndex.get(x);
        for (int i = 0; i < penztar.size(); i++) {
            if (i >= min && i <= max)
                kosar.add(penztar.get(i));
        }
    }

    public static void kosarMapFill(int x) {    //adott vasarlasnal indexeli a kosar tartalmat elöfordulas szerint
        kosarListFillById(x);
        for (String i : kosar) {
            if (kosarMap.containsKey(i)) {
                int oldval = (int) kosarMap.get(i);
                kosarMap.put(i, oldval + 1);
            } else
                kosarMap.put(i, 1);
        }
    }

    public static void task7(int x) {
        kosarMapFill(x);
        HashSet<String> kosarSet = new HashSet<String>(kosar);

        for (int i = 0; i < 20; i++) {
            for (String y : kosarSet) {
                if (i == (int) kosarMap.get(y))
                    System.out.println(i + " " + y);
            }
        }
        kosar.clear();
        kosarMap.clear();
        kosarSet.clear();
    }

    public static void writeToFile() throws IOException {
        int fizetesekSzama = 0;
        for (String i : penztar) {
            if (i.equals("F"))
                fizetesekSzama++;
        }
        BufferedWriter bw = new BufferedWriter(new FileWriter("osszeg.txt"));
        for (int i = 1; i <= fizetesekSzama; i++) {
            int kosarValue = 0;
            kosarMapFill(i);
            HashSet<String> kosarSet = new HashSet<String>(kosar);

            for (int c = 0; c < 20; c++) {
                for (String y : kosarSet) {
                    if (c == (int) kosarMap.get(y)) {
                        kosarValue += ertek(c);
                    }
                }
            }
            bw.write(i + ":" + kosarValue);
            bw.write("\n");
        }
        bw.close();
    }

    private static void sellDetails(int sellNumber) {
        System.out.println("Dani cucca");
        int sellIndex = 1;
        Map<String, Integer> soldProducts = new HashMap<>();
        for (String p : penztar) {
            if (p.equals("F")) {
                sellIndex++;
            }
            if (sellIndex == sellNumber && !p.equals("F")) {
                if (soldProducts.containsKey(p)) {
                    int number = soldProducts.get(p) + 1;
                    soldProducts.put(p, number);
                } else {
                    soldProducts.put(p, 1);
                }
            }
            if (sellIndex > sellNumber) {
                break;
            }
        }
        for (Map.Entry<String, Integer> entry : soldProducts.entrySet()) {
            System.out.println(entry.getKey() + " termékbõl " + entry.getValue() + " darabot vásároltak " + ertek(entry.getValue()) + "forint áron.");
        }
    }
}
