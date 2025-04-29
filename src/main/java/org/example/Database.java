package org.example;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Database {

    // Needs:
    //
    //  Read    Maze
    //      - single
    //      - all
    //  Write   Maze


    private static volatile Database helper;

    public static Database GetInstance() {
        Database localRef = helper;
        if(helper == null) {
            synchronized (SolverAPI.class) {
                localRef = helper;
                if(localRef == null) {
                    helper = localRef = new Database();
                }
            }
        }

        return localRef;
    }

    public void CreateDBExample() {

        var sql = "CREATE TABLE IF NOT EXISTS warehouses ("
        + "	id INTEGER PRIMARY KEY,"
        + "	name text NOT NULL,"
        + "	capacity REAL"
        + ");";

        // 
        try(var conn = DriverManager.getConnection("jdbc:sqlite:sample.db")) {
            System.out.println("Connection made");
            var stmt = conn.createStatement();
            stmt.execute(sql);

        } catch(SQLException e) {
            
        }
    }

    public void createMazeTable() {

        var sql = "CREATE TABLE IF NOT EXISTS mazes ("
                + "	maze_id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "	name text NOT NULL UNIQUE,"
                + "	maze_data text NOT NULL,"
                + " created_at text"
                + ");";

        try(var conn = DriverManager.getConnection("jdbc:sqlite:sample.db")) {
            System.out.println("Connection made");
            var stmt = conn.createStatement();
            stmt.execute(sql);
            System.out.println("Table created");

        } catch(SQLException e) {

        }
    }

    public void saveMaze(String name, int[][] mazeArray) {
        ObjectMapper mapper = new ObjectMapper();
        String jsonMaze = "";

        try {
            jsonMaze = mapper.writeValueAsString(mazeArray);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        String sql = "INSERT INTO mazes (name, maze_data, created_at) VALUES (?, ?, datetime('now'))";

        try(var conn = DriverManager.getConnection("jdbc:sqlite:sample.db")) {
            System.out.println("Connection made for inserting");

            var pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, name);
            pstmt.setString(2, jsonMaze);
            pstmt.executeUpdate();


        }catch (SQLException e) {
            e.printStackTrace();
            //handle mis-input here
        }


    }

    public List<Integer> getMazes(){
        List<Integer> mazes = new ArrayList<>();
        var sql = "SELECT * FROM mazes;";


        try(var conn = DriverManager.getConnection("jdbc:sqlite:sample.db")){
            System.out.println("Connection made for getting");

            var stmt = conn.createStatement();

            var rs = stmt.executeQuery(sql);

            while(rs.next()) {
                int mazeId = rs.getInt("maze_id");
                mazes.add(mazeId);

            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return mazes;
    }

    public List<Maze> getAllMazes(){
        ObjectMapper mapper = new ObjectMapper();
        List<Maze> mazes = new ArrayList<>();

        var sql = "SELECT * FROM mazes;";


        try(var conn = DriverManager.getConnection("jdbc:sqlite:sample.db")){
            System.out.println("Connection made for getting");

            var stmt = conn.createStatement();

            var rs = stmt.executeQuery(sql);

            while(rs.next()) {
                int mazeId = rs.getInt("maze_id");
                String mazeName = rs.getString("name");
                String mazeDate = rs.getString("created_at");
                String JsonMazeData = rs.getString("maze_data");
                int[][] mazeData = mapper.readValue(JsonMazeData, int[][].class);

                Maze maze = new Maze(mazeData, mazeData.length, mazeData[0].length);
                maze.setMazeID(mazeId);
                maze.setName(mazeName);
                maze.setDateMade(mazeDate);
                mazes.add(maze);

            }
        }catch (SQLException e){
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return mazes;
    }

    public void InsertToDbExample() {
        var names = new String[] {"Raw Materials", "Semifinished Goods", "Finished Goods"};
        var capacities = new int[] {3000,4000,5000};    

        String sql2 = "INSERT INTO warehouses(name,capacity) VALUES(?,?)";
        
        try(var conn = DriverManager.getConnection("jdbc:sqlite:sample.db")) {
            System.out.println("Connection made");
            var pstmt = conn.prepareStatement(sql2);

            for(int i = 0; i < 3; i++){
                pstmt.setString(1, names[i]);
                pstmt.setDouble(2, capacities[i]);
                pstmt.executeUpdate();
            }
        } catch(SQLException e) {
            
        }
    }

    public void SelectAllExample() {
        var sql3 = "SELECT id, name, capacity FROM warehouses";
        
        try(var conn = DriverManager.getConnection("jdbc:sqlite:sample.db")) {
            System.out.println("Connection made");
            var stmt = conn.createStatement();

            var rs = stmt.executeQuery(sql3);
            while (rs.next()) {
                System.out.printf("%-5s%-25s%-10s%n",
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getDouble("capacity")
                );
            }

        } catch(SQLException e) {
            
        }
    }
}
