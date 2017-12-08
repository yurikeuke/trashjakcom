<?php
  class Login_model extends CI_Model {
 
   public function cek($username, $password) {
        $this->db->where("email_user", $username);
        $this->db->where("password", $password);
        return $this->db->get("tj_user");
      }
 

  }
?>