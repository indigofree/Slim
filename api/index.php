<?php
/**
 * Created by PhpStorm.
 * User:
 * Date: 2016-07-25
 * Time: 오후 1:04
 */
//import Slim library
require "Slim/Slim.php";
//注册Slim框架自带的自动加载类
//register Slim auto loader
\Slim\Slim::registerAutoloader();
//initialize app
$app = new \Slim\Slim();
//connect to db
function getConnection(){
    $pdo = new PDO("mysql:host=localhost;dbname=test","root","root",array(PDO::MYSQL_ATTR_INIT_COMMAND=>"set names utf8"));
    $pdo->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
    return $pdo;
}
//show
$app->get('/','show');



//add
$app->post('/','add');
//delete
$app->delete('/:id','delete');

$app->get('/:search','searchHandle');
    
//get
function show(){
    $sql = "select * from tk_city order by CityID desc limit 10";

    try{
        $pdo = getConnection();
        $stat = $pdo->query($sql);
        $data =  $stat->fetchAll(PDO::FETCH_ASSOC);
        $pdo = null;
        //json trancoding
        foreach ($data as $key=>$value){
            foreach ($value as $k=>$v){
                $data[$key][$k] = urlencode($v);
            }
        }
        echo(urldecode(json_encode($data)));
    }catch (Exception $e){
        echo '{"err":'.$e->getMessage().'}';
    }
}

function searchHandle($search){
    $sql = "select * from tk_city where CityName like :search";
    try{
        $search = '%'.$search.'%';
        $pdo = getConnection();
        $stmt = $pdo->prepare($sql);
        $stmt->bindParam('search',$search);
        $stmt->execute();
        $data = $stmt->fetchAll(PDO::FETCH_ASSOC);

//        //json不支持中文,使用前先转码
//        foreach($data as $key=>$value){
//            foreach ($value as $k=>$v){
//                $data[$key][$k]=urlencode($v);
//            }
//        }
//        echo urldecode(json_encode($data));
        $pdo = null;
        echo json_encode($data);

    }catch (Exception $e){
        echo '{"err":'.$e->getMessage().'}';
    }
}

//post
function add(){
    $sql = "insert into tk_city(CityName,ZipCode,letter) values (:CityName,:ZipCode,:letter)";
    try{
        $pdo = getConnection();
        $stmt = $pdo->prepare($sql);
        $stmt->bindParam('CityName',$_POST['CityName']);
        $stmt->bindParam('ZipCode',$_POST['ZipCode']);
        $stmt->bindParam('letter',$_POST['letter']);
//        $stmt->bindParam('abbr',$_POST['abbr']);
        $stmt->execute();
        $id = $pdo->lastInsertId();
        $pdo = null;
        echo '{"id":'.$id.'}';

    }catch(Exception $e){
        echo '{"err":'.$e->getMessage().'}';
    }


}
$app->put('/:id',function ($id) use ($app){
    $sql = "update tk_city set CityName=:CityName,ZipCode=:ZipCode,letter=:letter where CityID=:CityID";
    //get the data transport from frontend
    $data = $app->request->put();
    try{
        $pdo = getConnection();
        $stmt = $pdo->prepare($sql);
        $stmt->bindParam('CityName',$data['CityName']);
        $stmt->bindParam('ZipCode',$data['ZipCode']);
        $stmt->bindParam('letter',$data['letter']);
//        $stmt->bindParam('abbr',$data['abbr']);
        $stmt->bindParam('CityID',$id);
        $stmt->execute();
        $pdo = null;
        echo json_encode($data);
    }catch(Exception $e){
        echo '{"err":'.$e->getMessage().'}';
    }

});

//delete
function delete($id){
    $sql = "delete from tk_city where CityID=:id";
    try{
        $pdo = getConnection();
        $stmt = $pdo->prepare($sql);
        $stmt->bindParam('id',$id);
        $stmt->execute();
        $pdo = null;
    }catch(Exception $e){
        echo '{"err":'.$e->getMessage().'}';
    }
}


//execute
$app->run();
