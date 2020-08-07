<%@ page import="java.sql.*, java.io.*"%>
<%@ page import="com.oreilly.servlet.*"%>
<%@ page import="com.oreilly.servlet.multipart.*"%>
<%
 request.setCharacterEncoding("UTF-8");
 String uploadPath="c:/Temp/";

 MultipartRequest multi = new MultipartRequest(
    request,
    uploadPath,
    1024 * 1024 * 10,
    "UTF-8",
    new DefaultFileRenamePolicy()
 );
 String code=multi.getParameter("code");
 String pname=multi.getParameter("pname");
 int price=Integer.parseInt(multi.getParameter("price"));
 String image=multi.getFilesystemName("image");
 try{
    Class.forName("com.mysql.jdbc.Driver");
    Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/webdb", "root", "1234");
    String sql="insert into productinfo(code, pname, price, image) values(?,?,?,?)";
    PreparedStatement ps=con.prepareStatement(sql);
    ps.setString(1, code);
    ps.setString(2, pname);
    ps.setInt(3, price);
    ps.setString(4, image);
    ps.execute();
 }catch(Exception e){
    System.out.println(e.toString());
    response.setStatus(400);
 }
%>