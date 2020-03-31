import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.TreeSet;

public class Main {
    static class Movie {
        String name;
        int year;
        
        public Movie(String name, int year) {
            this.name = name;
            this.year = year;
        }
    }
    
    public static void main(String[] args) {
        String filename = "C:src\\main\\movies.csv";
        Scanner sc;
        
        try {
            sc = new Scanner(new File(filename));
            sc.nextLine();
            HashMap<String, ArrayList<Movie>> map = new HashMap<>();
            TreeMap<Integer, ArrayList<Movie>> treemap = new TreeMap<>();
            
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                String[] tokens = line.split(",");
                
                if (tokens.length > 3) {
                    int q1 = line.indexOf("\"");
                    int q2 = line.indexOf("\"", q1 + 1);
                    tokens[1] = line.substring(q1 + 1, q2);
                    tokens[2] = line.substring(q2 + 2);
                }

                String[] genres = tokens[2].split("\\|");
                String title = tokens[1];
                int r = title.lastIndexOf(")");
                int l = title.lastIndexOf("(", r - 1);
                int year;
                if (r > 0) {
                    String yearString = title.substring(l + 1, r);
                    if (yearString.contains("â€“")) {
                        year = Integer.parseInt(yearString.substring(0,
                        yearString.indexOf("â€“")));
                } 
                else {
                    year = Integer.parseInt(yearString);
                }
                title = title.substring(0, l - 1);
            } 
            else {
                year = 0;
            }

            Movie m = new Movie(title, year);
                for (String genre : genres) {
                    if (!map.containsKey(genre)) {
                        map.put(genre, new ArrayList<>());
                    }
                    map.get(genre).add(m);
                    if (r > 0) {
                        if (!treemap.containsKey(year)) {
                            treemap.put(year, new ArrayList<>());
                        }
                        treemap.get(year).add(m);
                    }
                }
            }
            
            System.out.println("Movies under each genre for whole data set: ");
                for (String genre : map.keySet()) {
                    System.out.printf("(%s,%d) ", genre, map.get(genre).size());
                }
                
            System.out.println();
            System.out.println("Movies under each genre for most recent 5 years: ");
                for (String genre : map.keySet()) {
                    int count = 0;
                    for (Movie m : map.get(genre)) {
                        if (m.year >= 2015) {
                            count++;
                        }
                    }
                    System.out.printf("(%s,%d) ", genre, count);
                }
                
                System.out.println();
                TreeSet<String> genres = new TreeSet<>(map.keySet());
                    for (int year : treemap.keySet()) {
                        System.out.print(year + ": ");
                            for (String genre : genres) {
                                ArrayList<Movie> moviesOfYear = new
                                ArrayList<>(treemap.get(year));
                                moviesOfYear.retainAll(map.get(genre));
                                System.out.printf("(%s,%d) ", genre, moviesOfYear.size());
                            }
                        System.out.println();
                    }
                    
                sc.close();
        } 
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
