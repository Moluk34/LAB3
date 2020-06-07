import net.proteanit.sql.DbUtils;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
        final JComboBox jComboBox1 = new JComboBox();
        try {
            DatabaseMetaData meta = c.getMetaData();
            rs = meta.getTables(null, null, null, new String[]{"TABLE"});

            while (rs.next()) {
                String tableName = rs.getString("TABLE_NAME");
                jComboBox1.addItem(tableName);
            }
            jComboBox1.updateUI();
        } catch(SQLException e) {
            JOptionPane.showMessageDialog(null,e);
        }
        //tworzenie okna
        final JFrame f= new JFrame("LAB3");
        f.setSize(1024,780);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JOptionPane.showMessageDialog(null,"Polaczono z baza");

        //tworzenie tabeli
        final JTable table = new JTable();
        JScrollPane scrollPane = new JScrollPane(table);
        table.setFillsViewportHeight(true);
        f.getContentPane().add(BorderLayout.CENTER, table);
        f.getContentPane().add(BorderLayout.SOUTH, jComboBox1);
        f.setVisible(true);
        table.setVisible(true);



        //filtracja
        JPanel jp2 = new JPanel(); //tworzymy panel
        JLabel jl2 = new JLabel("filtracja"); //dajemy etykietke filtracja, zeby uzytkownik wiedzial co to

        JButton jb2 = new JButton("Filtruj");
        final JTextField tf21 = new JTextField("od");
        final JTextField tf22 = new JTextField("do");



        // TODO: zmienić, żeby czytało za każdym razem jak się zmieni tabelę. Można w teorii wrzucić w pętlę od słuchania boxa 1
        final JComboBox jComboBox2 = new JComboBox();


        jp2.add(jl2);
        jp2.add(jComboBox2);
        jp2.add(tf21);
        jp2.add(tf22);
        jp2.add(jb2);
        f.getContentPane().add(BorderLayout.NORTH,jp2);

        ////


        //testowy fragment, który powinien wyświetlać tabele, ale odpala sie tylko raz na starcie, wiec wrzuca 1 tabele. trzeba zrobic jakos, zeby sie odpalał przy kazdej zmianie
            //ten fragment opisany u góry jest wrzucony w blok, który nasłuchuje zmiany w tym rozwijanym kwadraciku.
        final Connection finalC = c;
        final Connection finalC1 = c;
        final Connection finalC2 = c;
        jComboBox1.addActionListener (new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                try {
                    Statement stmt1 = finalC.createStatement();
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
                    tm.addRow(new Object[]{});
                    tm.fireTableDataChanged();


                    rs1.close();
                    stmt1.close();

                   updateBox up = new updateBox();
                    up.updateBOX(finalC2,jComboBox2,jComboBox1);

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null,ex);
                }
            }
        }
        );
        //DefaultTableModel model = (DefaultTableModel) table.getModel();
       // model.addRow(new Object[]{"Column 1", "Column 2", "Column 3"});



            // rysowanie tabeli od nowa, gdy kliknie się filtruj, czyli wywołanie nowego selecta z where

            jb2.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent evt) {
                    try {
                        Statement stmt1 = finalC.createStatement();
                        ResultSet rs1 = stmt1.executeQuery("SELECT * FROM [" + jComboBox1.getSelectedItem() + "]  WHERE [" +jComboBox2.getSelectedItem() + "] BETWEEN " + tf21.getText() + " AND " + tf22.getText() +";");

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
                        tm.addRow(new Object[]{});
                        tm.fireTableDataChanged();


                        rs1.close();
                        stmt1.close();

                        updateBox up = new updateBox();
                        up.updateBOX(finalC2,jComboBox2,jComboBox1);

                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(null,ex);
                    }
                }
            });
            /////////////Koniec tego fragmentu.

    }
 };
;

