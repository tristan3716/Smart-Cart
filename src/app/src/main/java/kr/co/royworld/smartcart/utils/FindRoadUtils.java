package kr.co.royworld.smartcart.utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kr.co.royworld.smartcart.model.Block;
import kr.co.royworld.smartcart.model.Position;

public class FindRoadUtils {
    // g:비용
    // h:거리
    // f:합 (f = g + h)
    private String[][] map;
    private Position cpos;
    private Position tpos;

    private JSONObject mapInfo;
    private List<Block> blocks;

    private List<Position> borders;
    private Position[][] astars;
    private boolean srchYn;

    private Map<String, Position> directs = new HashMap<>();

    public FindRoadUtils(JSONObject mapInfo, List<Block> blocks) throws JSONException {
        this.mapInfo = mapInfo;
        this.blocks = blocks;

        directs.put("top", new Position(0, -1, 10));        // 위
        directs.put("rightTop", new Position(1, -1, 14));   // 우위
        directs.put("right", new Position(1, 0, 10));       // 우
        directs.put("rightBottom", new Position(1, 1, 14)); // 우하
        directs.put("bottom", new Position(0, 1, 10));      // 하
        directs.put("leftBottom", new Position(-1, 1, 14)); // 좌하
        directs.put("left", new Position(-1, 0, 10));       // 좌
        directs.put("leftTop", new Position(-1, -1, 14));   // 좌위

        map = new String[mapInfo.getInt("height")][mapInfo.getInt("width")];
        for(int i = 0; i < mapInfo.getInt("height"); i++){
            for(int j = 0;j < mapInfo.getInt("width");j++){
                if(checkBlcok(new Position(j, i, -1)))    map[i][j] = "X";
                else                                           map[i][j] = "O";
            }
        }
    }

    public List<Position> searchRoute(Position cpos, Position tpos){
        this.cpos = cpos;
        this.tpos = tpos;

        map[cpos.getPosY()][cpos.getPosX()] = "S";
        map[tpos.getPosY()][tpos.getPosX()] = "E";

        astars = null;
        borders = null;
        srchYn = false;

        if(cpos.getPosX() != tpos.getPosX() || cpos.getPosY() != tpos.getPosY()){
            findRoad(cpos);
        }

        return getRoad();
    }

    private List<Position> getRoad(){
        if(srchYn){
            List<Position> roads = new ArrayList<>();
            Position pos = astars[tpos.getPosY()][tpos.getPosX()];
            while(true){
                roads.add(pos);

                if(pos == null || (pos.getPosX() == cpos.getPosX() && pos.getPosY() == cpos.getPosY()))    break;

                pos = astars[pos.getPrev().getPosY()][pos.getPrev().getPosX()];
            }

            // reverse
            List<Position> result = new ArrayList<>();
            for(int i = roads.size() - 1; i >= 0; i--){
                result.add(roads.get(i));
            }
            return result;
        }else{
            return null;
        }
    }

    // Astar 계산
    private void findRoad(Position cur){
        if(directs != null){
            for(String key:directs.keySet()){
                makeAstar(cur, directs.get(key));
            }

            // 다음 최적 경로 찾기
            if(borders != null && !srchYn){
                Position next = null;
                int nextIdx = -1;
                for(int i = 0; i < borders.size(); i++){
                    Position border = borders.get(i);
                    if(next == null || border.getAstarF() <= next.getAstarF()){
                        next = border;
                        nextIdx = i;
                    }
                }
                if(next != null){
                    borders.remove(nextIdx);
                    findRoad(next);
                }
            }
        }
    }

    private void makeAstar(Position prev, Position direct){
        // Step01: 위치이동
        int posX = prev.getPosX() + direct.getPosX();
        int posY = prev.getPosY() + direct.getPosY();

        // Step03: 예외처리
        if(posX < 0 || posY < 0 || posY > map.length - 1 || posX > map[posY].length -1)    return;    // 범위를 넘어간 경우 X


        // Step03: 데이터 공간이 없으면 할당
        if(astars == null)          astars = new Position[map.length][map[0].length];
        if(astars[posY][posX] != null){
            Position pos = astars[posY][posX];
            if(pos.getPrev() != null){
                if(pos.getPrev().getPosX() == prev.getPosX() && pos.getPrev().getPosY() == prev.getPosY())
                    // 이전 위치인 경우 계산 X
                    return;
            }else{
                // 시작점인경우 계산 X
                return;
            }
        }
        else if(map[posY][posX].equals("X"))         return;    // 장애물이 있으면 스킵
        // else if(map[posY][posX] == "T")           return;    // 사전에 지나갔던 자리면 스킵

        // 비용 및 거리 계산
        if(map[prev.getPosY()][prev.getPosX()].equals("S")){
            astars[prev.getPosY()][prev.getPosX()] = new Position(prev.getPosX(), prev.getPosY(), -1, 0, 0, 0);
        }
        int gradeG = astars[prev.getPosY()][prev.getPosX()].getAstarG() + (int)direct.getDistance();
        int gradeH = (Math.abs(tpos.getPosX() - posX) + Math.abs(tpos.getPosY() - posY) - 1) * 10;
        if(astars[posY][posX] == null || astars[posY][posX].getAstarF() > gradeG + gradeH){
            // 이 경로를 지나간 적이 없거나 경로 계산값이 이전보다 작은 경우
            Position inData = new Position(posX, posY, -1, gradeG + gradeH, gradeG, gradeH);
            inData.setPrev(prev);
            astars[posY][posX] = inData;

            if(borders == null)    borders = new ArrayList<>();
            borders.add(astars[posY][posX]);
        }
        if(map[posY][posX].equals("E")){
            srchYn = true;
        }
        return;
    }

    private boolean checkBlcok(Position cur) {
        for(Block block:blocks){
            if(block.isBlock(cur)){
                return true;
            }
        }
        return false;
    }

}
