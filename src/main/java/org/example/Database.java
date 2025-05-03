package org.example;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Database {

    private static volatile Database helper;
    private final String databaseUrl = "jdbc:sqlite:sample.db";

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

    public void createMazeTable() {
        String sql = "CREATE TABLE IF NOT EXISTS mazes ("
                + "	maze_id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "	name text NOT NULL UNIQUE,"
                + "	maze_data text NOT NULL,"
                + " created_at text"
                + ");";

        try(var conn = DriverManager.getConnection(databaseUrl)) {
            var stmt = conn.createStatement();
            stmt.execute(sql);
            System.out.println("Table created");

        } catch(SQLException e) {

        }
    }

    public boolean saveMaze(String name, int[][] mazeArray) {
        ObjectMapper mapper = new ObjectMapper();
        String jsonMaze = "";

        try {
            jsonMaze = mapper.writeValueAsString(mazeArray);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        String sql = "INSERT INTO mazes (name, maze_data, created_at) VALUES (?, ?, datetime('now'))";

        try(var conn = DriverManager.getConnection(databaseUrl)) {
            System.out.println("Connection made for saving");

            var pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, name);
            pstmt.setString(2, jsonMaze);
            pstmt.executeUpdate();

        }catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public List<Maze> getMazes(){
        ObjectMapper mapper = new ObjectMapper();
        List<Maze> mazes = new ArrayList<>();

        String sql = "SELECT * FROM mazes;";


        try(var conn = DriverManager.getConnection(databaseUrl)){
            System.out.println("Connection made for getting");

            var stmt = conn.createStatement();

            var rs = stmt.executeQuery(sql);

            while(rs.next()) {
                int mazeId = rs.getInt("maze_id");
                String mazeName = rs.getString("name");
                String mazeDate = rs.getString("created_at");
                String JsonMazeData = rs.getString("maze_data");
                int[][] mazeData = mapper.readValue(JsonMazeData, int[][].class);

                Maze maze = new Maze(mazeData, mazeData[0].length, mazeData.length);
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
}
