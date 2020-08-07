<%@page import="java.sql.*, java.io.*"%>
<%
 response.setCharacterEncoding("UTF-8");
 String uploadPath="c:/Temp/";
 String image="";      
 try{
    Class.forName("com.mysql.jdbc.Driver");
    Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/webdb", "root", "1234");

    String code=request.getParameter("code");
    String sql="select * from productinfo where code=?";
   
    PreparedStatement ps=con.prepareStatement(sql);
    ps.setString(1, code);
    ResultSet rs=ps.executeQuery();
    if(rs.next()){
      image=rs.getString("image");
    }

    sql="delete from productinfo where code=?";
    ps=con.prepareStatement(sql);
    ps.setString(1, code);
    ps.execute();

    File file=new File(uploadPath + image);
    if(file.exists()){
      file.delete();
    }
 }catch(Exception e){
   System.out.println(e.toString());
   response.setStatus(400);
 }
%>