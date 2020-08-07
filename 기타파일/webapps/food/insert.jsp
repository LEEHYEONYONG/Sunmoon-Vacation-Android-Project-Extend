<%@page import="java.sql.*"%>
<%
    request.setCharacterEncoding("UTF-8");
    try{
        Class.forName("com.mysql.jdbc.Driver");
        Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/webdb", "root", "1234");

        String name=request.getParameter("name");
        String tel=request.getParameter("tel");
        String address=request.getParameter("address");
        String latitude=request.getParameter("latitude");
        String longitude=request.getParameter("longitude");

        String sql="insert into foodinfo(name, tel, address, latitude, longitude) values(?,?,?,?,?)";

        PreparedStatement ps=con.prepareStatement(sql);
        ps.setString(1, name);
        ps.setString(2, tel);
        ps.setString(3, address);
        ps.setDouble(4, Double.parseDouble(latitude));
        ps.setDouble(5, Double.parseDouble(longitude));
        ps.execute();
    }catch(Exception e){
        response.setStatus(400);
    }
%>