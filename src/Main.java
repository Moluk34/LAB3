import net.proteanit.sql.DbUtils;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class Main {


    public static void main(String[]args){
        connecttoDB();





    }

    public static void connecttoDB() { //wszystko tutaj wrzuciłem, bo nie znam javy na na tyle, zeby to umiec podzielic


        //polaczenie z baza danych
        Connection c = null;
        Statement s = null;

        try {
            c = DriverManager.getConnection("jdbc:sqlite:E:\\LAB3_PBD\\lab3.db");   //sqlite wskazuje RDBMS, w którym baza, z którą zostanie nawiązane połączenie, została utworzona (należy dostosować)
            if (c.isValid(0)) System.out.println("Połączono z bazą danych!");
            else System.out.println("Brak połączenia z baza danych!");
            //c.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }



        //combobox do wybrania tabeli
        ResultSet rs = null;
        JComboBox jComboBox1 = new JComboBox();
        try {
            DatabaseMetaData meta = c.getMetaData();
            rs = meta.getTables(null, null, null, new String[]{"TABLE"});

            while (rs.next()) {
                String tableName = rs.getString("TABLE_NAME");
                jComboBox1.addItem(tableName);
            }
            jComboBox1.updateUI();
        } catch(SQLException e) {

        }
        //tworzenie okna
        JFrame f= new JFrame("LAB3");
        f.setSize(1024,780);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JOptionPane.showMessageDialog(null,"Polaczono z baza");

        //tworzenie tabeli
        JTable table = new JTable();
        JScrollPane scrollPane = new JScrollPane(table);
        table.setFillsViewportHeight(true);
        f.getContentPane().add(BorderLayout.CENTER, table);
        f.getContentPane().add(BorderLayout.SOUTH, jComboBox1);
        f.setVisible(true);
        table.setVisible(true);



        //testowy fragment, który powinien wyświetlać tabele, ale odpala sie tylko raz na starcie, wiec wrzuca 1 tabele. trzeba zrobic jakos, zeby sie odpalał przy kazdej zmianie

        try {
            Statement stmt1 = c.createStatement();
            ResultSet rs1 = stmt1.executeQuery("SELECT * FROM [" + jComboBox1.getSelectedItem() + "];");

            // get columns info
            ResultSetMetaData rsmd = rs1.getMetaData();
            int columnCount = rsmd.getColumnCount();

            // for changing column and row model
            DefaultTableModel tm = (DefaultTableModel) table.getModel();

            // clear existing columns
            tm.setColumnCount(0);

            // add specified columns to table
            for (int i = 1; i <= columnCount; i++ ) {
                tm.addColumn(rsmd.getColumnName(i));
            }

            // clear existing rows
            tm.setRowCount(0);

            // add rows to table
            while (rs1.next()) {
                String[] a = new String[columnCount];
                for(int i = 0; i < columnCount; i++) {
                    a[i] = rs1.getString(i+1);
                }
                tm.addRow(a);
            }
            tm.fireTableDataChanged();

            rs1.close();
            stmt1.close();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null,ex);
        }

        //dalej jest git










    }



    public static void updateTable() {

/*        PreparedStatement pst = null;
        ResultSet rs = null;
        Connection c = null;
        connecttoDB();
        String sql = "SELECT * FROM filmy";
        try {
            pst = c.prepareStatement(sql);
            rs = pst.executeQuery();
            table.setModel(DbUtils.resultSetToTableModel(rs));
        } catch (Exception e){
            JOptionPane.showMessageDialog(null,e);
        }*/
    }

}