<?php
include_once('../config.php');
header('Content-Type: application/json');


    $sql = "
		SELECT COUNT(*) AS dt
		FROM yramlcom_tra5hjak.tj_requests
		WHERE
			is_removed = '0';
	";

	$result = $conn->query($sql);
	$rows = $result->fetch_assoc();
	
	if($rows['dt'] >= 1) {
	    $sql = "
			SELECT *
			FROM
				 yramlcom_tra5hjak.tj_requests
			WHERE
				is_removed = '0';
		";
	
    	$mysqli = $conn->query($sql);
    	$data = array();
    	$i = 0;
    	$date = date('Y-m-d');
    	while($row = $mysqli->fetch_array()) {
    	  $data[$i]['id_laporan'] 	= $row['id_laporan'];
	      $data[$i]['name'] 		= $row['name'];
	      $data[$i]['email'] 		= $row['email'];
	      $data[$i]['date'] 		= $row['date'];
	      $data[$i]['locaction'] 	= $row['locaction'];
	      $data[$i]['rt'] 			= $row['rt'];
	      $data[$i]['rw'] 			= $row['rw'];
	      $data[$i]['cities'] 		= $row['cities'];
	      $data[$i]['district'] 	= $row['district'];
	      $data[$i]['villages'] 	= $row['villages'];
	      $data[$i]['provincies'] 	= $row['provincies'];
	      $data[$i]['description'] 	= $row['description'];
	      $data[$i]['attachment'] 	= $row['attachment'];
	      $data[$i]['status'] 		= $row['status'];
    	 $i++;
    	}
    	
    	$return['status_code'] = '000';
    	$return['status'] = 'Sukses';
    	$return['data'] = $data;
	} else {
	    $return['status_code']   ='101';
        $return['status']        ='Data Invalid';
        $return['error']         = 'No data found';
	}

	echo json_encode($return);
?>