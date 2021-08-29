<?php
	
	include "soyuz78asz6.php";
	class usr{}
	$listImage = $_POST["dataImage"];
	$gambarString = "";
	$splitData = explode("II0713novalraihan", $listImage);
	$e = 0;
	$jumlahFile = count($splitData)-1;
	for($x = 0; $x < $jumlahFile; $x++){
		$path = "data_image/".$x.round(microtime(true) * 1000).".jpg";
		$gambarString .= $x.round(microtime(true) * 1000).".jpg"."||07n";
    	file_put_contents($path, base64_decode($splitData[$x]));
		$e++;
	}
	if($e == $jumlahFile){
		$query = mysqli_query($conn, "INSERT INTO data_image VALUES (null, '".$gambarString."');");
		if($query){
			$id = mysqli_insert_id($conn);
			$response = new usr();
			$response->success = 1;
			$response->message = "Berhasil Upload";
			die(json_encode($response));
		}else{
			$response = new usr();
			$response->success = 0;
			$response->message = "Gagal";
			die(json_encode($response));
		}
	}
	mysqli_close($conn);
?>