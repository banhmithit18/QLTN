package forms;

import models.entities.*;
import utils.DBConnection;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CellRoomForm extends JDialog {
    DefaultTableModel model;
    private JTable table;
    private JTable tableUpdate;
    private ArrayList<prisonerlist> list;
    public CellRoomForm() {
        setDefaultCloseOperation(2);
        setIconImage(Toolkit.getDefaultToolkit().getImage("code\\QLTN\\src\\models\\image\\polic.png"));
        setTitle("CellRoom Form");
        setModal(true);
        setBounds(250, 200, 1101, 350);
        setLayout(null);
        JPanel contentPane = new JPanel();
        setContentPane(contentPane);
        contentPane.setLayout(null);
        contentPane.setBackground(Color.DARK_GRAY);

        JMenuBar mnb=new JMenuBar();
        setJMenuBar(mnb);

        JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        tabbedPane.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 10));
        tabbedPane.setToolTipText("");
        tabbedPane.setBounds(0, 0, 1101, 350);
        contentPane.add(tabbedPane);

        //-------------------------------PANEL ADD----------------------------
        JPanel panelAdd = new JPanel();
        tabbedPane.addTab("Add", null, panelAdd, null);
        JLabel lblId = new JLabel("ID:");
        panelAdd.setBackground(Color.DARK_GRAY);
        lblId.setForeground(Color.white);
        lblId.setBounds(51, 37, 47, 13);
        panelAdd.add(lblId);
        panelAdd.setLayout(null);

        JLabel lblIdCellRoom = new JLabel();
        lblIdCellRoom.setBounds(188, 49, 80, 13);
        panelAdd.add(lblIdCellRoom);
        DBConnection db = new DBConnection();
        int lastestIdCellRoom = db.getLastIdCellRoom()+1;
        JLabel lblLastIdCellRoom = new JLabel(String.valueOf(lastestIdCellRoom));
        lblLastIdCellRoom.setBounds(180,30,50,25);
        lblLastIdCellRoom.setForeground(Color.white);
        panelAdd.add(lblLastIdCellRoom);

        JLabel lblCellRoomName = new JLabel("CellRoom Name:");
        lblCellRoomName.setBounds(51,74, 100, 13);
        lblCellRoomName.setForeground(Color.white);
        panelAdd.add(lblCellRoomName);

        JTextField tftCellRoomName = new JTextField();
        tftCellRoomName.setBounds(188, 74,142, 21);
        panelAdd.add(tftCellRoomName);

        JLabel lblCellroomType = new JLabel("CellRoom Type:");
        lblCellroomType.setBounds(51, 110, 100, 13);
        lblCellroomType.setForeground(Color.white);
        panelAdd.add(lblCellroomType);
        String stringCellroomtype = "";
        if(db.checkTable("cellroomtype")) {
            stringCellroomtype = "Select," + db.getAllName("cellroomtype");
        }
        String[] cellroomtype = stringCellroomtype.split(",");
        JComboBox cbCellroomtype = new JComboBox(cellroomtype);
        JSplitPane splitCellroomtype = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitCellroomtype.add(cbCellroomtype);
        splitCellroomtype.setBounds(188, 110,142, 21);
        panelAdd.add(splitCellroomtype);


        JButton btnSave=new JButton(new ImageIcon("code\\QLTN\\src\\models\\image\\save.png"));
        btnSave.setBounds(145,175,90,30);
        btnSave.setText("Save");
        panelAdd.add(btnSave);
        btnSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int id=db.getLastIdCellRoom()+1;
                String crn=tftCellRoomName.getText();
                String crt=cbCellroomtype.getSelectedItem().toString();
//                Integer crt=Integer.parseInt(cbCellroomtype.getSelectedItem().toString());
                String urlConnection = "jdbc:sqlserver://localhost:1433;databaseName=QLTN;user=sa;password=123456";
                try(Connection con = DriverManager.getConnection(urlConnection)){
                    if(!crn.equals("")){
                        if(!crt.equals("Select")){
                            Statement stmt = con.createStatement();
                            String query = "INSERT INTO cellroom VALUES(N'" +  crn + "'," + db.getColumnID("cellroomtype", crt) + ")";
                            int check = stmt.executeUpdate(query);
                            if(check != 0)
                            {
                                JOptionPane.showMessageDialog(cbCellroomtype, "Add Success !!");
                                lblLastIdCellRoom.setText(String.valueOf(lastestIdCellRoom + 1));
                                tftCellRoomName.setText("");
                                cbCellroomtype.setSelectedIndex(0);
                            }else {
                                JOptionPane.showMessageDialog(cbCellroomtype, "Add Fail !!");
                            }
                        }else {
                            JOptionPane.showMessageDialog(cbCellroomtype, "Cellroom Type incorrect !!");
                        }
                    }else {
                        JOptionPane.showMessageDialog(cbCellroomtype, "Cellroom Name incorrect !!");
                    }
                }catch (SQLException a) {
                    a.printStackTrace();
                }
                getListCellroom();
                showTableCellRoom();
            }
        });
        //-------------------------------PANEL UPDATE----------------------------

        JPanel panelUpdate = new JPanel();
        tabbedPane.addTab("Update", null, panelUpdate, null);
        panelUpdate.setLayout(null);
        panelUpdate.setBackground(Color.DARK_GRAY);

        JLabel lblCellroomID = new JLabel("cellroomID:");
        lblCellroomID.setForeground(Color.white);
        lblCellroomID.setBounds(34, 70, 74, 13);
        panelUpdate.add(lblCellroomID);

        JTextField tftCellroomID = new JTextField();
        tftCellroomID.setBounds(118, 67, 96, 19);
        panelUpdate.add(tftCellroomID);
        tftCellroomID.setColumns(10);
        tftCellroomID.setEditable(false);

        JLabel lblCellroomname = new JLabel("cellroomName:");
        lblCellroomname.setForeground(Color.white);
        lblCellroomname.setBounds(34, 119, 74, 13);
        panelUpdate.add(lblCellroomname);


        JTextField tftCellroomNameUpdate = new JTextField();
        tftCellroomNameUpdate.setColumns(10);
        tftCellroomNameUpdate.setBounds(118, 116, 96, 19);
        panelUpdate.add(tftCellroomNameUpdate);
        tftCellroomNameUpdate.setEditable(false);

        JLabel lblCellroomtype = new JLabel("cellroomType:");
        lblCellroomtype.setForeground(Color.white);
        lblCellroomtype.setBounds(34, 167, 74, 13);
        panelUpdate.add(lblCellroomtype);

        JTextField tftCellroomType = new JTextField();
        tftCellroomType.setColumns(10);
        tftCellroomType.setBounds(118, 164, 96, 19);
        panelUpdate.add(tftCellroomType);

        JButton btnUpdate = new JButton(new ImageIcon("code\\QLTN\\src\\models\\image\\update.png"));
        btnUpdate.setText("Update");
        btnUpdate.setBounds(86, 206, 115, 34);
        panelUpdate.add(btnUpdate);

        JScrollPane scrollPaneUpdate = new JScrollPane();
        scrollPaneUpdate.setBounds(284, 63, 356, 154);
        panelUpdate.add(scrollPaneUpdate);


        tableUpdate = new JTable();
        tableUpdate.setFillsViewportHeight(true);
        tableUpdate.setDefaultEditor(Object.class, null);
        tableUpdate.setBackground(Color.DARK_GRAY);
        tableUpdate.setForeground(Color.white);
        tableUpdate.setModel(new DefaultTableModel(
                new Object[][] {
                        {null, null, null},
                },
                new String[] {
                        "cellroomid", "cellroomname", "cellroomtype"
                }
        ));
        getListCellroom();
        showTableCellRoom();
        scrollPaneUpdate.setViewportView(tableUpdate);
        tableUpdate.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selectedRow = tableUpdate.getSelectedRow();
                DefaultTableModel modelUpdate = (DefaultTableModel) tableUpdate.getModel();
                tftCellroomID.setText(modelUpdate.getValueAt(selectedRow,0).toString());
                tftCellroomNameUpdate.setText(modelUpdate.getValueAt(selectedRow,1).toString());
                tftCellroomType.setText(modelUpdate.getValueAt(selectedRow,2).toString());
            }
        });
        btnUpdate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                    try{
                        if(!tftCellroomType.getText().equals("")){
//                            if(db.checkcellroomtype()){
                            int cellroomid = Integer.parseInt(tftCellroomID.getText());
                            int cellroomtype = Integer.parseInt(tftCellroomType.getText());
                            if (db.checkcellroomtype(cellroomid) != cellroomtype) {
                                String connectionUrl = "jdbc:sqlserver://localhost:1433;databaseName=QLTN;user=sa;password=123456";
                                Connection conn= DriverManager.getConnection(connectionUrl);
                                String query = "UPDATE cellroom SET cellroomtype = " + tftCellroomType.getText() + " WHERE cellroomid = " + tftCellroomID.getText();
                                Statement stmt = conn.createStatement();
                                int check = stmt.executeUpdate(query);
                                if(check != 0){
                                    JOptionPane.showMessageDialog(tftCellroomNameUpdate, "Update Success !!");
                                }else {
                                    JOptionPane.showMessageDialog(tftCellroomNameUpdate, "Update Fail!!");
                                }
                            }else {
                                JOptionPane.showMessageDialog(tftCellroomNameUpdate,cellroomtype + " is the current cellroom type !!");
                            }

                        }else {
                            JOptionPane.showMessageDialog(tftCellroomNameUpdate, "CellroomType incorrect !!");
                        }
                    } catch (Exception throwables) {
                        throwables.printStackTrace();
                    }
                    getListCellroom();
                    showTableCellRoom();;
            }
        });

        //-------------------------------PANEL SEARCH----------------------------

        JPanel panelSearch = new JPanel();
        tabbedPane.addTab("Search", null, panelSearch, null);
        panelSearch.setBackground(Color.DARK_GRAY);
        panelSearch.setLayout(null);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBackground(Color.DARK_GRAY);
        scrollPane.setBounds(10, 168, 1064, 178);
        panelSearch.add(scrollPane);


        table = new JTable();
        table.setFillsViewportHeight(true);
        table.setBackground(Color.DARK_GRAY);
        table.setForeground(Color.white);
        table.setDefaultEditor(Object.class, null);
        table.setModel(new DefaultTableModel(

                new Object[][] {
                },
                new String[] {
                        "prisonerid","prisoneridcard", "prisonername", "prisonerage", "gender", "dateofbirth", "dateofarrest", "crime", "dangerlevel", "punishment", "cellroom", "address", "city", "country"
                }
        ));
        getListPrisoner();
        showTable();


        table.getColumnModel().getColumn(7).setPreferredWidth(47);
        table.getColumnModel().getColumn(8).setPreferredWidth(69);
        table.getColumnModel().getColumn(9).setPreferredWidth(84);
        table.getColumnModel().getColumn(10).setPreferredWidth(53);
        table.getColumnModel().getColumn(11).setPreferredWidth(50);
        scrollPane.setViewportView(table);


        JLabel lblCellRoomSearch = new JLabel("CellRoom:");
        lblCellRoomSearch.setForeground(Color.white);
        lblCellRoomSearch.setBounds(22, 47, 87, 13);
        panelSearch.add(lblCellRoomSearch);
        String stringCellroom = "";
        if(db.checkTable("cellroom")) {
            stringCellroom = "All," + db.getAllNameCellroom("cellroom");
        }
        final String[][] cellroom = {stringCellroom.split(",")};
        JComboBox cbCellroom = new JComboBox(cellroom[0]);
        JSplitPane splitCellroom = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitCellroom.add(cbCellroom);
        splitCellroom.setBounds(119, 43, 120, 21);
        panelSearch.add(splitCellroom);



        JButton btnSearch = new JButton(new ImageIcon("code\\QLTN\\src\\models\\image\\search.png"));
        btnSearch.setText("  Search");
        btnSearch.setFont(new Font("Arial", Font.PLAIN, 15));
        btnSearch.setBounds(266, 35, 140, 40);
        panelSearch.add(btnSearch);
        btnSearch.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DBConnection db=new DBConnection();
                String crt = cbCellroom.getSelectedItem().toString();
                if(crt.equals("All")){
                    getListPrisoner();
                    showTable();
                }else {
                    Integer cellroom = db.getColumnID("cellroom", crt);
                    getListPrisonerFind(cellroom);
                    showFind(cellroom);
                }
            }
            public void showTable(){
                model = (DefaultTableModel) table.getModel();
                model.setRowCount(0);

                list = getListPrisoner();
                for (prisonerlist p : list) {
                    model.addRow(new Object[]{
                             p.getPrisonerid(), p.getPrisoneridcard(), p.getPrisonername(),p.getPrisonerage(), p.getGender(), p.getDateofbirth(), p.getDateofarrest(), p.getCrime(), p.getDangerlevel(), p.getPunishment(), p.getCellroom(), p.getAddress(), p.getCity(), p.getCountry(),
                    });
                }
            }

            public ArrayList<prisonerlist> getListPrisoner(){
                ArrayList<prisonerlist> list=new ArrayList<>();
                String sql = "select prisonerid, prisoneridcard, prisonername, prisonerage, gender, convert(nvarchar, dateofbirth, 103) as dateofbirth,convert(nvarchar, dateofarrest, 103) as dateofarrest, crime, dangerlevel, punishment, cellroom, address, city, country from prisoner";

                try{
                    String connectionUrl = "jdbc:sqlserver://localhost:1433;databaseName=QLTN;user=sa;password=123456";
                    Connection conn= DriverManager.getConnection(connectionUrl);
                    PreparedStatement ps = conn.prepareStatement(sql);
                    ResultSet rs = ps.executeQuery();
                    while (rs.next()){
                        prisonerlist p = new prisonerlist();
                        p.setPrisonerid(rs.getInt("prisonerid"));
                        p.setPrisoneridcard(rs.getString("prisoneridcard"));
                        p.setPrisonername(rs.getString("prisonername"));
                        p.setPrisonerage(rs.getInt("prisonerage"));
                        p.setGender(rs.getString("gender"));
                        p.setDateofbirth(rs.getString("dateofbirth"));
                        p.setDateofarrest(rs.getString("dateofarrest"));
                        p.setCrime(rs.getInt("crime"));
                        p.setDangerlevel(rs.getInt("dangerlevel"));
                        p.setPunishment(rs.getInt("punishment"));
                        p.setCellroom(rs.getInt("cellroom"));
                        p.setAddress(rs.getString("address"));
                        p.setCity(rs.getInt("city"));
                        p.setCountry(rs.getInt("country"));
                        list.add(p);
                    }
                } catch (Exception throwables) {
                    throwables.printStackTrace();
                }
                return list;
            }

            public ArrayList<prisonerlist> getListPrisonerFind(Integer cellroom){
                ArrayList<prisonerlist> listfind=new ArrayList<>();
                String sql = "select prisonerid, prisoneridcard, prisonername, prisonerage, gender, convert(nvarchar, dateofbirth, 103) as dateofbirth,convert(nvarchar, dateofarrest, 103) as dateofarrest, crime, dangerlevel, punishment, cellroom, address, city, country from prisoner where cellroom = " + cellroom;

                try{
                    String connectionUrl = "jdbc:sqlserver://localhost:1433;databaseName=QLTN;user=sa;password=123456";
                    Connection conn= DriverManager.getConnection(connectionUrl);
                    PreparedStatement ps = conn.prepareStatement(sql);
                    ResultSet rs = ps.executeQuery();
                    while (rs.next()){
                        prisonerlist p = new prisonerlist();
                        p.setPrisonerid(rs.getInt("prisonerid"));
                        p.setPrisoneridcard(rs.getString("prisoneridcard"));
                        p.setPrisonername(rs.getString("prisonername"));
                        p.setPrisonerage(rs.getInt("prisonerage"));
                        p.setGender(rs.getString("gender"));
                        p.setDateofbirth(rs.getString("dateofbirth"));
                        p.setDateofarrest(rs.getString("dateofarrest"));
                        p.setCrime(rs.getInt("crime"));
                        p.setDangerlevel(rs.getInt("dangerlevel"));
                        p.setPunishment(rs.getInt("punishment"));
                        p.setCellroom(rs.getInt("cellroom"));
                        p.setAddress(rs.getString("address"));
                        p.setCity(rs.getInt("city"));
                        p.setCountry(rs.getInt("country"));
                        listfind.add(p);
                    }
                } catch (Exception throwables) {
                    throwables.printStackTrace();
                }
                return listfind;
            }
            public void showFind(Integer cellroom){
                model = (DefaultTableModel) table.getModel();
                model.setRowCount(0);
                list = getListPrisonerFind(cellroom);
                for (prisonerlist p : list) {
                    model.addRow(new Object[]{
                            p.getPrisonerid(), p.getPrisoneridcard(), p.getPrisonername(),p.getPrisonerage(), p.getGender(), p.getDateofbirth(), p.getDateofarrest(), p.getCrime(), p.getDangerlevel(), p.getPunishment(), p.getCellroom(), p.getAddress(), p.getCity(), p.getCountry()
                    });
                }
            }
        });

        setVisible(true);
    }
    public void showTableCellRoom(){
        model = (DefaultTableModel) tableUpdate.getModel();
        model.setRowCount(0);

        List<models.entities.cellroom> listCellRoom = getListCellroom();
        for (cellroom c : getListCellroom()) {
            model.addRow(new Object[]{
                    c.getCellroomid(), c.getCellroomname(), c.getCellroomtype(),
            });
        }
    }
    public void showTable(){
        model = (DefaultTableModel) table.getModel();
        model.setRowCount(0);

        list = getListPrisoner();
        for (prisonerlist p : list) {
            model.addRow(new Object[]{
                    p.getPrisonerid(), p.getPrisoneridcard(), p.getPrisonername(),p.getPrisonerage(), p.getGender(), p.getDateofbirth(), p.getDateofarrest(), p.getCrime(), p.getDangerlevel(), p.getPunishment(), p.getCellroom(), p.getAddress(), p.getCity(), p.getCountry()
            });
        }
    }
    public ArrayList<prisonerlist> getListPrisoner(){
        ArrayList<prisonerlist> list=new ArrayList<>();
        String sql = "select prisonerid, prisoneridcard, prisonername, prisonerage, gender, convert(nvarchar, dateofbirth, 103) as dateofbirth,convert(nvarchar, dateofarrest, 103) as dateofarrest, crime, dangerlevel, punishment, cellroom, address, city, country from prisoner";


        try{
            String connectionUrl = "jdbc:sqlserver://localhost:1433;databaseName=QLTN;user=sa;password=123456";
            Connection conn= DriverManager.getConnection(connectionUrl);
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                prisonerlist p = new prisonerlist();
                p.setPrisonerid(rs.getInt("prisonerid"));
                p.setPrisoneridcard(rs.getString("prisoneridcard"));
                p.setPrisonername(rs.getString("prisonername"));
                p.setPrisonerage(rs.getInt("prisonerage"));
                p.setGender(rs.getString("gender"));
                p.setDateofbirth(rs.getString("dateofbirth"));
                p.setDateofarrest(rs.getString("dateofarrest"));
                p.setCrime(rs.getInt("crime"));
                p.setDangerlevel(rs.getInt("dangerlevel"));
                p.setPunishment(rs.getInt("punishment"));
                p.setCellroom(rs.getInt("cellroom"));
                p.setAddress(rs.getString("address"));
                p.setCity(rs.getInt("city"));
                p.setCountry(rs.getInt("country"));
                list.add(p);
            }
        } catch (Exception throwables) {
            throwables.printStackTrace();
        }
        return list;
    }
    public ArrayList<cellroom> getListCellroom(){
        ArrayList<cellroom> listCellRoom=new ArrayList<>();
        String sql = "select * from cellroom";

        try{
            String connectionUrl = "jdbc:sqlserver://localhost:1433;databaseName=QLTN;user=sa;password=123456";
            Connection conn= DriverManager.getConnection(connectionUrl);
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                cellroom c = new cellroom();
                c.setCellroomid(rs.getInt("cellroomid"));
                c.setCellroomname(rs.getString("cellroomname"));
                c.setCellroomtype(rs.getInt("cellroomtype"));
                listCellRoom.add(c);
            }
        } catch (Exception throwables) {
            throwables.printStackTrace();
        }
        return listCellRoom;
    }
    public ArrayList<prisonerlist> getListPrisonerFind(Integer cellroom){
        ArrayList<prisonerlist> listfind=new ArrayList<>();
        String sql = "select prisonerid, prisoneridcard, prisonername, prisonerage, gender, convert(nvarchar, dateofbirth, 103) as dateofbirth,convert(nvarchar, dateofarrest, 103) as dateofarrest, crime, dangerlevel, punishment, cellroom, address, city, country from prisoner where cellroom = " + cellroom;

        try{
            String connectionUrl = "jdbc:sqlserver://localhost:1433;databaseName=QLTN;user=sa;password=123456";
            Connection conn= DriverManager.getConnection(connectionUrl);
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                prisonerlist p = new prisonerlist();
                p.setPrisonerid(rs.getInt("prisonerid"));
                p.setPrisoneridcard(rs.getString("prisoneridcard"));
                p.setPrisonername(rs.getString("prisonername"));
                p.setPrisonerage(rs.getInt("prisonerage"));
                p.setGender(rs.getString("gender"));
                p.setDateofbirth(rs.getString("dateofbirth"));
                p.setDateofarrest(rs.getString("dateofarrest"));
                p.setCrime(rs.getInt("crime"));
                p.setDangerlevel(rs.getInt("dangerlevel"));
                p.setPunishment(rs.getInt("punishment"));
                p.setCellroom(rs.getInt("cellroom"));
                p.setAddress(rs.getString("address"));
                p.setCity(rs.getInt("city"));
                p.setCountry(rs.getInt("country"));
                listfind.add(p);
            }
        } catch (Exception throwables) {
            throwables.printStackTrace();
        }
        return listfind;
    }
    public void showFind(Integer cellroom){
        model = (DefaultTableModel) table.getModel();
        model.setRowCount(0);
        list = getListPrisonerFind(cellroom);
        for (prisonerlist p : list) {
            model.addRow(new Object[]{
                    p.getPrisonerid(), p.getPrisoneridcard(), p.getPrisonername(),p.getPrisonerage(), p.getGender(), p.getDateofbirth(), p.getDateofarrest(), p.getCrime(), p.getDangerlevel(), p.getPunishment(), p.getCellroom(), p.getAddress(), p.getCity(), p.getCountry()
            });
        }
    }
}
