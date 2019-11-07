package kr.co.royworld.smartcart.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import kr.co.royworld.smartcart.model.Block;
import kr.co.royworld.smartcart.model.Position;
import kr.co.royworld.smartcart.utils.FindRoadUtils;
import kr.co.royworld.smartcart.utils.LogUtils;

public class MapView extends View {
    private JSONObject mapInfo;
    private List<Block> blocks;
    private Position cpos, tpos;
    private String[] blockNames;

    private FindRoadUtils utils;
    private List<Position> routes;


    public MapView(Context context)  throws JSONException{
        super(context); // 부모의 인자값이 있는 생성자를 호출한다

        init();
    }

    public MapView(Context context, AttributeSet attrs)  throws JSONException{
        super(context, attrs);

        init();
    }

    public MapView(Context context, AttributeSet attrs, int defStyle) throws JSONException{
        super(context, attrs);

        init();
    }

    private void init() throws JSONException {
        mapInfo = new JSONObject();
        mapInfo.put("width", 12);
        mapInfo.put("height", 10);
        mapInfo.put("xgrade", 1);
        mapInfo.put("ygrade", 1);

        blocks = new ArrayList<>();
        blocks.add(new Block(0, 3, 0, 5));    // 음료
        blocks.add(new Block(1, 6, 1, 8));    // 카운터
        blocks.add(new Block(3, 4, 3, 5));    // 냉동식품
        blocks.add(new Block(3, 6, 3, 7));    // 아이스크림
        blocks.add(new Block(5, 4, 5, 7));    // 커피/차
        blocks.add(new Block(7, 4, 7, 7));    // 조미료
        blocks.add(new Block(9, 4, 9, 7));    // 통조림
        blocks.add(new Block(11, 4, 11, 7));  // 과자류
        blocks.add(new Block(8, 2, 9, 2));    // EMPTY
        blocks.add(new Block(10, 2, 11, 2));  // EMPTY
        blocks.add(new Block(4, 0, 5, 0));    // 생선
        blocks.add(new Block(6, 0, 11, 0));   // 육류
        blocks.add(new Block(4, 9, 5, 9));    // 과일
        blocks.add(new Block(6, 9, 11, 9));   // 야채

        blockNames = new String[]{
                "음료"
                , "카운터"
                , "냉동\n식품"
                , "아이\n스크림"
                , "커피\n/\n차"
                , "조미료"
                , "통조림"
                , "과자류"
                , ""
                , ""
                , "생선"
                , "육류"
                , "과일"
                , "야채"
        };

        utils = new FindRoadUtils(mapInfo, blocks);
    }

    public boolean isNear(){
        if(cpos != null && tpos != null){
            if(cpos.getPosX() == tpos.getPosX() && cpos.getPosY() == tpos.getPosY())    return true;    // 현재위치랑 겹치는 경우
            if(routes != null && routes.size() < 3)                                     return true;    // 현재위치 바로 앞인 경우
        }

        return false;
    }

    public void setCpos(Position cpos){
        try{
            this.cpos = cpos;
            this.invalidate();
        }catch(Exception e){

        }
    }

    public Position getCpos(){
        return this.cpos;
    }

    public void setTpos(Position tpos){
        try{
            this.tpos = tpos;
            this.invalidate();
        }catch(Exception e){

        }
    }

    public void setTouchCpos(float rateX, float rateY){
        try{
            int mapWidth = mapInfo.getInt("width");
            int width  = this.getWidth();
            int height = this.getHeight();
            int gab = (width/mapWidth - 10)/3;

            int curX = Math.round(mapInfo.getInt("width") * (rateX - gab) / width);
            int curY = Math.round(mapInfo.getInt("height") * (rateY - gab) / height);
            if(gab >= rateX)    curX = 0;
            if(gab >= rateY)    curY = 0;

            setCpos(new Position(curX, curY));
        }catch(JSONException e){

        }
    }

    @Override
    protected void onDraw(Canvas canvas) { // 화면을 그려주는 작업
        try{
            drawMap(canvas);
        }catch(JSONException e){

        }
    }

    private void drawMap(Canvas canvas) throws JSONException{
        int pinWidth = 1;
        int pinHeight = 1;

        int width  = canvas.getWidth();
        int height = canvas.getHeight();
        int mapWidth = mapInfo.getInt("width");
        int mapHeight = mapInfo.getInt("height");

        int xgrade = width/mapWidth;
        int ygrade = height/mapHeight;

        pinWidth = pinWidth * xgrade - 10;
        pinHeight = pinHeight * xgrade - 10;

        mapInfo.put("xgrade", xgrade);
        mapInfo.put("ygrade ", ygrade);

        // Astar 알고리즘 시작
        /*
        map = new Array(mapInfo.height);
        for(var i = 0; i < mapInfo.height; i++){
            map[i] = new Array(mapInfo.width);
            for(var j = 0;j < mapInfo.width;j++){
                if(checkBlcok(j, i))    map[i][j] = "X";
                else                    map[i][j] = "O";
            }
        }

        if(cpos)    map[cpos.posY][cpos.posX] = "S";
        if(tpos){
            map[tpos.posY][tpos.posX] = "E";
            roads = searchRoute(cpos);
        }
        */
        // Astar 알고리즘 종료

        setBackgroundColor(Color.parseColor("#FFFFFF")); // 배경색을 지정

        Paint paint = new Paint(); // 화면에 그려줄 도구를 셋팅하는 객체

        // 지도이미지
        for(int i = 0; i < blocks.size(); i++){
            Block block = blocks.get(i);

            // 블락 생성
            paint.setColor(Color.parseColor("#9A9A9A"));     // 색상을 지정
            int x1 = block.getX1() * xgrade;
            int y1 = block.getY1() * ygrade;
            int x2 = block.getX2() * xgrade + pinWidth;
            int y2 = block.getY2() * ygrade + pinHeight;
            canvas.drawRect(x1, y1, x2, y2, paint);

            paint.setColor(Color.WHITE);
            paint.setTextSize(32f);

            int xGap = 15;
            if(blockNames[i].split("\n").length > 1){
                int posY = y1 + pinHeight/2;
                for(String str:blockNames[i].split("\n")){
                    canvas.drawText(str, x1 + xGap, posY, paint);
                    posY += 50;
                }
            }else{
                canvas.drawText(blockNames[i], x1 + xGap, y1 + pinHeight/2, paint);
            }


            // canvas.drawRect(100,100,200,200,paint);
            // canvas.drawCircle(300, 300, 40, paint); // 원의중심 x,y, 반지름,paint

            /*
            paint.setColor(Color.YELLOW);
            paint.setStrokeWidth(10f);    // 선의 굵기
            canvas.drawLine(400, 100, 500, 150, paint); // 직선
            */

            // path 자취 만들기
            /*
            Path path = new Path();
            path.moveTo(20, 100); // 자취 이동
            path.lineTo(100, 200); // 자취 직선
            path.cubicTo(150, 30, 200, 300, 300, 200); // 자취 베이지곡선
            paint.setColor(Color.MAGENTA);
            canvas.drawPath(path, paint);
            */
        }

        if(this.cpos != null && this.tpos != null){
            routes = utils.searchRoute(this.cpos, this.tpos);
            LogUtils.debug("CPOS > " + cpos.toString());
            LogUtils.debug("TPOS > " + cpos.toString());
            if(routes != null){
                if(routes.size() < 1)    routes = utils.searchRoute(this.cpos, this.tpos);

                Path path = new Path();
                int pathGap =  + pinWidth/3 + 15;
                path.moveTo(cpos.getPosX() * xgrade + pathGap, cpos.getPosY() * ygrade + pathGap); // 자취 이동
                for(Position pos:routes){
                    path.lineTo(pos.getPosX() * xgrade + pathGap, pos.getPosY() * ygrade + pathGap); // 자취 직선
                    // path.cubicTo(150, 30, 200, 300, 300, 200); // 자취 베이지곡선
                    paint.setStrokeWidth(10f);
                    paint.setStyle(Paint.Style.STROKE);
                    paint.setColor(Color.MAGENTA);
                    paint.setPathEffect(new DashPathEffect(new float[]{10, 10, 10, 10}, 0));
                    canvas.drawPath(path, paint);
                }
            }
        }

        paint.setStyle(Paint.Style.FILL);
        if(cpos != null){
            paint.setColor(Color.BLUE);
            paint.setAlpha(100);    // 0 - 225
            int cposX = cpos.getPosX() * xgrade + pinWidth/3 + 15;
            int cposY = cpos.getPosY() * ygrade + pinWidth/3 + 15;
            canvas.drawCircle(cposX, cposY, pinWidth/3, paint); // 원의중심 x,y, 반지름,paint
        }

        if(tpos != null){
            paint.setColor(Color.RED);
            paint.setAlpha(100);    // 0 - 225
            int cposX = tpos.getPosX() * xgrade + pinWidth/3 + 15;
            int cposY = tpos.getPosY() * ygrade + pinWidth/3 + 15;
            canvas.drawCircle(cposX, cposY, pinWidth/3, paint); // 원의중심 x,y, 반지름,paint
        }
    }
}
