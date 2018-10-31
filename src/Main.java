import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

public class Main {

    // Zähler für Anfragen, wer wem folgt
    static int followerRequests;

    public static void main(String[] args) {
        ArrayList<String> textFiles = listAllFiles(new File("superstar"));
        for(String file : textFiles){ //für jede Datei wird der Superstar gesucht
            followerRequests = 0;
            System.out.println("Searching for superstar in file '" + file + "'...");
            superstar(file);
        }
    }

    /**
     * @param folder Ordner der durchsucht werden soll
     * @return Liste mit Dateien im angegebenen Ordner
     *
     * Listet alle Dateien in einem gegebenen Ordner um sie später zu verarbeiten
     */
    private static ArrayList<String> listAllFiles(File folder){
        ArrayList<String> textFiles = new ArrayList<>();
        File[] fileNames = folder.listFiles();

        assert fileNames != null;
        for(File file : fileNames){
            if(file.isDirectory()){
                listAllFiles(file);
            }else{
                textFiles.add(file.getAbsolutePath());
            }
        }

        return textFiles;
    }

    /**
     * @param filename Datei in der nach dem Superstar gesucht werden soll
     *
     * Sucht nach einem Superstar in einer gegebenen Datei
     */
    private static void superstar(String filename){
        HashMap<String, Account> accounts = new HashMap<>();
        ArrayList<Account> potentialSuperstars = new ArrayList<>();
        boolean superstarFound = false;

        readAccountsFromFile(filename, accounts);
        for(String key : accounts.keySet()){
            Account account = accounts.get(key);
            if (checkIfPotentialSuperstar(account, accounts))
                potentialSuperstars.add(account);
        }

        for(Account account : potentialSuperstars){
            if(checkIfSuperstar(account, accounts)){
                superstarFound = true;
                System.out.println(account.getName() + " is the superstar!");
                break;
            }
        }

        if(!superstarFound)
            System.out.println("There is no superstar");

        System.out.println(followerRequests + " follower requests are needed.");
    }

    /**
     * @param account Zu überprüfender Account
     * @param accounts Alle Accounts
     * @return Ob der zu überprüfende Account der Superstar ist
     *
     *
     */
    private static boolean checkIfSuperstar(Account account, HashMap<String, Account> accounts){
        for(String key : accounts.keySet()){
            if(!key.equals(account.getName())){ // nur prüfen wenn es sich bei dem aktuellen Account nicht um den zu prüfenden Account handelt
                Account followerAccount = accounts.get(key);
                if(!account.isFollower(followerAccount)) return false; //wenn der Account einem anderen folgt kann er nicht der Superstar sein, also false und Abbruch.
            }
        }

        //wenn der Account keinem anderen folgt ist er der Superstar
        return true;
    }

    /**
     * @param account Zu überprüfender Account
     * @param accounts Alle Accounts
     * @return Ob der zu überprüfende Account Superstar sein kann
     *
     *
     */
    private static boolean checkIfPotentialSuperstar(Account account, HashMap<String, Account> accounts){
        for(String key : accounts.keySet()){
            if(!key.equals(account.getName())){ // nur prüfen wenn es sich bei dem aktuellen Account nicht um den zu prüfenden Account handelt
                Account followingAccount = accounts.get(key);
                if(followingAccount.isFollower(account)){
                    return false; //wenn ein Account dem zu prüfenden Account nicht folgt kann er nicht der Superstar sein, also false und Abbruch.
                }
            }
        }

        //wenn alle Accounts dem Account folgen kann er Superstar sein
        return true;
    }

    /**
     * @param fileName einzulesende Datei
     * @param accounts Liste in welche dei Accounts gespeichert werden
     *
     * Liest Accounts aus einer gegebenen Datei ein
     */
    private static void readAccountsFromFile(String fileName, HashMap<String, Account> accounts){
        String names = null; //erste Zeile mit Namen
        ArrayList<String> lines = new ArrayList<>(); //restliche Zeilen mit X folgt Y

        //liest Zeilen aus Datei ein
        try(BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            for(String line; (line = br.readLine()) != null; ) {
                //erste Zeile enthält Namen und muss gesondert gespeichert werden
                if (names == null)
                    names = line;
                else
                    lines.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        //erstellt Accounts aus Namen und Zeilen
        if(names != null)
            createAccounts(names, lines, accounts);

    }

    /**
     * @param names Accountnamen durch Leerzeichen getrennt
     * @param lines Zeilen, welche die Follower-Beziehungen beinhalten
     * @param accounts Liste, in welche die Accounts gespeichert werden sollen.
     *
     * erstellt Accounts und Follower-Beziehungen aus der Liste der Follower-Beziehungen und Accounts
     */
    private static void createAccounts(String names, ArrayList<String> lines, HashMap<String, Account> accounts){
        //erstellt Accounts für jeden Namen
        for(String name : names.split(" ")) // teilt die Zeile in Namen auf und durchläuft sie
            accounts.put(name, new Account(name));

        for(String followerRelation : lines){
            String[] parts = followerRelation.split(" ");
            if (parts.length == 2){
                //weißt den ersten Account dem zweiten als Follower zu
                String follower = parts[0];
                String followed = parts[1];

                Account followingAccount = accounts.get(follower);
                Account followedAccount = accounts.get(followed);

                followedAccount.addFollower(followingAccount);
            }
        }
    }
}
