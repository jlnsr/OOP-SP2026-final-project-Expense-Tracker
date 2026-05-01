public class Main{
    public static void main(String[] args){
        String[] headers = {"Col1", "Col2", "Col3"};
        String[][] data = {
            {"Hello", "Hola", "Pershendetje"}
        };
        Table table = new Table(
            10, 2, 3, 
            headers, data);
        table.renderTable();
        Out.println("Success");
        // SUCCESS? Y
    }
}