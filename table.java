public class Table {
    private int colWidth;
    private int colGap;
    private int numColumns; 
    private int totalWidth;
    private String[] headers;
    private String[][] data;

    Table(int colWidth, int colGap, int numColumns, String[] headers, String[][] data){
        this.colWidth = colWidth;
        this.colGap = colGap;
        this.numColumns = numColumns;
        this.totalWidth = (colWidth+colGap) * numColumns + (numColumns-1);
        this.headers = headers;
        this.data = data;
    }
    
    private void drawRow(String[] fields){
        /*
        |field1 |... |fieldN |
        +--------------------+
        */
        for (String f:fields){
            System.out.printf("|%-"+(colWidth+2)+"s", f);
        }
        Out.println("");
        Out.print("+");
        Out.printMany("-", this.totalWidth);
        Out.println("+");
    }
    
    private void drawRows(String[][] data){
        for (String[] record:data){
            /*
            |field1 |... |fieldN
            +------------------
            */
            drawRow(record);
        }
    }

    private void drawHeaders(String[] columns){
        /*
        ______________
        |col1   |...    |colN
        +-------------
        */
        Out.printMany("-", this.totalWidth+2);
        Out.println("");
        drawRow(columns);
    }

    public void renderTable(){
        drawHeaders(headers);
        drawRows(data);
    }


    /*
    Update first record with 'fieldValue' in 'col'.
    Locates index of said record. 
    **/
    /*public String[] find(String targetVal, String targetCol){
        // determine column index based on column name 'col'
        int colIndex = 0;
        for(int i=0; i<numColumns; i++){
            if(headers[i].equals(targetCol)){
                colIndex = i;
                break;
            }
        }
        // search table for record with 'fieldValue' at 'colIndex'
        for(String[] record:this.data){
            for(int i=0; i<numColumns; i++){
                if(i==colIndex && record[i].equals(targetVal)){
                    return record;
                }
            }
        }
    }*/
}
