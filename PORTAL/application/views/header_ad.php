<!DOCTYPE html>
<html>
  <head>
   <?php include 'application/views/header.php';?>
    <script type="text/javascript" src="<?php echo base_url();?>/assets/js/plugins/jquery.vmap.min.js"></script>
    <script type="text/javascript" src="<?php echo base_url();?>/assets/js/plugins/jquery.vmap.world.js"></script>
    <script type="text/javascript" src="<?php echo base_url();?>/assets/js/plugins/jquery.vmap.sampledata.js"></script>
     <script type="text/javascript" src="<?php echo base_url();?>/assets/js/plugins/chart.js"></script>
     <script type="text/javascript" src="https://www.gstatic.com/charts/loader.js"></script>
  </head>
  <body class="sidebar-mini fixed">
    <div class="wrapper">
      <!-- Navbar-->
      <header class="main-header hidden-print"><a class="logo" href="trashjak.com"><img src="<?php echo base_url();?>assets/images/logo-tj.png" alt="Logo" title="trashjak" width="130px"></a>
        <nav class="navbar navbar-static-top">
          <!-- Sidebar toggle button--><a class="sidebar-toggle" href="#" data-toggle="offcanvas"></a>
          <!-- Navbar Right Menu-->
          <div class="navbar-custom-menu">
            <ul class="top-nav">
     
              <!-- User Menu-->
              <li class="dropdown"><a class="dropdown-toggle" href="#" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false"><i class="fa fa-user fa-lg"></i></a>
                <ul class="dropdown-menu settings-menu">
                  <li><a href="<?php echo base_url('admin/logout'); ?>"><i class="fa fa-sign-out fa-lg"></i> Logout</a></li>
                </ul>
              </li>
            </ul>
          </div>
        </nav>
      </header>
      <!-- Side-Nav-->
      <aside class="main-sidebar hidden-print">
        <section class="sidebar">
          <div class="user-panel">
            <div class="pull-left image"><img class="img-circle" src="https://s3.amazonaws.com/uifaces/faces/twitter/jsa/48.jpg" alt="User Image"></div>
            <div class="pull-left info">
              <p><?php echo $this->session->userdata("name"); ?></p>
              <p class="designation"><?php echo $this->session->userdata("role"); ?></p>
            </div>
          </div>
          <!-- Sidebar Menu-->
          <ul class="sidebar-menu">
            <li class="active"><a href="<?php echo base_url();?>admin/dashboard"><i class="fa fa-dashboard"></i><span>Dashboard</span></a></li>
            <li><a href="<?php echo base_url();?>admin/list_request"><i class="fa fa-edit"></i><span>Daftar Request</span></a></li>    
          </ul>
        </section>
      </aside>