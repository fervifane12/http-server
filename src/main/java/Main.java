public class Main {
    public static void main(String[] args) {
        String baseDirectory = null;
        if (args.length== 2 && args[0].equals("--directory")){
            baseDirectory = args[1];
        }

        new StartServer(4221, baseDirectory).start();
    }
}
