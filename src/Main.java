public class Main {
    public static void main(String[] args) {
        StateOfClobber st1 = new StateOfClobber(10,10);
        StateOfClobber st2 = st1.copy();
        System.out.println("----");
        System.out.println(st1);
        System.out.println(st2);

        st2.printBoard();
    }
}