package com.example.demo;

import com.sun.syndication.feed.synd.SyndContent;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

@Service
public class Test {

    @Autowired
    private final NoticiaRepository noticiaRepository;

    public Test(NoticiaRepository noticiaRepository) {
        this.noticiaRepository = noticiaRepository;
    }

    public void salvaNoticiasBanco() throws Exception {

        String feed = "https://g1.globo.com/dynamo/musica/rss2.xml";
//
        URL feedUrl = new URL(feed);

        SyndFeedInput input = new SyndFeedInput();
        SyndFeed sf = input.build(new XmlReader(feedUrl));

        List entries = sf.getEntries();
        Iterator it = entries.iterator();

        while (it.hasNext()) {
            Noticia noticia = new Noticia();
            SyndEntry entry = (SyndEntry) it.next();
            System.out.println(entry.getTitle());
            System.out.println(entry.getLink());
            SyndContent description = entry.getDescription();
            System.out.println(description.getValue());
            System.out.println();

            noticia.setTitulo(entry.getTitle());
            noticia.setDescricao(description.getValue());
            noticia.setLink(entry.getLink());

            this.noticiaRepository.save(noticia);
        }
    }


    public SortedMap<Integer, Noticia> ranking(String titulo){
        List<Noticia> noticias = this.noticiaRepository.findAll();

        SortedMap<Integer, Noticia> porcentagem = new TreeMap<>();

        List<Noticia> noticiasListadas = new ArrayList<>();

        noticias.forEach(noticia -> {
            porcentagem.put(lock_match(noticia.getTitulo(), titulo), noticia);
        });

        return porcentagem;
    }



        public static int lock_match(String s, String t) {

        int totalw = word_count(s);
        int total = 100;
        int perw = total / totalw;
        int gotperw = 0;

        if (!s.equals(t)) {

            for (int i = 1; i <= totalw; i++) {
                if (simple_match(split_string(s, i), t) == 1) {
                    gotperw = ((perw * (total - 10)) / total) + gotperw;
                } else if (front_full_match(split_string(s, i), t) == 1) {
                    gotperw = ((perw * (total - 20)) / total) + gotperw;
                } else if (anywhere_match(split_string(s, i), t) == 1) {
                    gotperw = ((perw * (total - 30)) / total) + gotperw;
                } else {
                    gotperw = ((perw * smart_match(split_string(s, i), t)) / total) + gotperw;
                }
            }
        } else {
            gotperw = 100;
        }
        return gotperw;
    }

    public static int anywhere_match(String s, String t) {
        int x = 0;
        if (t.contains(s)) {
            x = 1;
        }
        return x;
    }

    public static int front_full_match(String s, String t) {
        int x = 0;
        String tempt;
        int len = s.length();

        //----------Work Body----------//
        for (int i = 1; i <= word_count(t); i++) {
            tempt = split_string(t, i);
            if (tempt.length() >= s.length()) {
                tempt = tempt.substring(0, len);
                if (s.contains(tempt)) {
                    x = 1;
                    break;
                }
            }
        }
        //---------END---------------//
        if (len == 0) {
            x = 0;
        }
        return x;
    }

    public static int simple_match(String s, String t) {
        int x = 0;
        String tempt;
        int len = s.length();


        //----------Work Body----------//
        for (int i = 1; i <= word_count(t); i++) {
            tempt = split_string(t, i);
            if (tempt.length() == s.length()) {
                if (s.contains(tempt)) {
                    x = 1;
                    break;
                }
            }
        }
        //---------END---------------//
        if (len == 0) {
            x = 0;
        }
        return x;
    }

    public static int smart_match(String ts, String tt) {

        char[] s = new char[ts.length()];
        s = ts.toCharArray();
        char[] t = new char[tt.length()];
        t = tt.toCharArray();


        int slen = s.length;
        //number of 3 combinations per word//
        int combs = (slen - 3) + 1;
        //percentage per combination of 3 characters//
        int ppc = 0;
        if (slen >= 3) {
            ppc = 100 / combs;
        }
        //initialising an integer to store the total % this class genrate//
        int x = 0;
        //declaring a temporary new source char array
        char[] ns = new char[3];
        //check if source char array has more then 3 characters//
        if (slen < 3) {
        } else {
            for (int i = 0; i < combs; i++) {
                for (int j = 0; j < 3; j++) {
                    ns[j] = s[j + i];
                }
                if (cross_full_match(ns, t) == 1) {
                    x = x + 1;
                }
            }
        }
        x = ppc * x;
        return x;
    }

     public static int  cross_full_match(char[] s, char[] t) {
        int z = t.length - s.length;
        int x = 0;
        if (s.length > t.length) {
            return x;
        } else {
            for (int i = 0; i <= z; i++) {
                for (int j = 0; j <= (s.length - 1); j++) {
                    if (s[j] == t[j + i]) {
                        // x=1 if any charecer matches
                        x = 1;
                    } else {
                        // if x=0 mean an character do not matches and loop break out
                        x = 0;
                        break;
                    }
                }
                if (x == 1) {
                    break;
                }
            }
        }
        return x;
    }

    public static String split_string(String s, int n) {

        int index;
        String temp;
        temp = s;
        String temp2 = null;

        int temp3 = 0;

        for (int i = 0; i < n; i++) {
            int strlen = temp.length();
            index = temp.indexOf(" ");
            if (index < 0) {
                index = strlen;
            }
            temp2 = temp.substring(temp3, index);
            temp = temp.substring(index, strlen);
            temp = temp.trim();

        }
        return temp2;
    }

    public static int word_count(String s) {
        int x = 1;
        int c;
        s = s.trim();
        if (s.isEmpty()) {
            x = 0;
        } else {
            if (s.contains(" ")) {
                for (;;) {
                    x++;
                    c = s.indexOf(" ");
                    s = s.substring(c);
                    s = s.trim();
                    if (s.contains(" ")) {
                    } else {
                        break;
                    }
                }
            }
        }
        return x;
    }


}
//
//        String feed = "https://g1.globo.com/dynamo/musica/rss2.xml";
//
//        URL feedUrl = new URL(feed);
//
//        SyndFeedInput input = new SyndFeedInput();
//        SyndFeed sf = input.build(new XmlReader(feedUrl));
//
//        List entries = sf.getEntries();
//        Iterator it = entries.iterator();
//
//        while (it.hasNext()) {
//            SyndEntry entry = (SyndEntry) it.next();
//            System.out.println(entry.getTitle());
//            System.out.println(entry.getLink());
//            SyndContent description = entry.getDescription();
//            System.out.println(description.getValue());
//            System.out.println();
//        }
//    }
//}