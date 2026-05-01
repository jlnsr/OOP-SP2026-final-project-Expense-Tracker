public final class Out{
    private static void print(boolean line, String... args){
        if(args.length==1){
            if(line){
                System.out.println(args[0]);    
            }else {
                System.out.print(args[0]);
            }
            return;
        }
       
        System.out.print(args[0]);
        for (int i=1; i<args.length; i++){
            System.out.print(" "+args[i]);    
        }

        if(line){
            System.out.print('\n');
        }
    }
    public static void print(String... args){
        print(false, args);
    }
    public static void println(String... args){
        print(true, args);
    }
    public static void printMany(String arg, int reps){
        for (int i=0; i<reps; i++){
            print(arg);
        }
    }
}