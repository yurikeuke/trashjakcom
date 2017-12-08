<div class="content-wrapper">
 <!-- <?php var_dump($detail[0]->name);?> -->
        <div class="row">
          <div class="col-md-12">
            <div class="card">
              <div class="card-body">
                <section class="invoice">
                <div class="row">
                  <div class="col-xs-12">
                    <h2 class="page-header"><i class="fa fa-download"></i> REQUEST ID#<?php echo ($detail[0]->id_laporan.' ('.$detail[0]->status.')');?><small class="pull-right">Date: <?php echo ($detail[0]->date);?></small></h2>
                  </div>
                </div>
                <div class="row invoice-info">
                  <div class="col-xs-4">Pelapor
                    <address><strong><?php echo ($detail[0]->name);?></strong><br><?php echo ($detail[0]->locaction);?><br><?php echo ('RT '.$detail[0]->rt.', RW '.$detail[0]->rw);?><br><?php echo ($detail[0]->villages.', '.$detail[0]->district.', '.$detail[0]->cities.', '.$detail[0]->provincies);?><br>Email: <?php echo ($detail[0]->email);?></address>
                  </div>
                </div>
                <div class="row">
                  <div class="col-xs-12 table-responsive">
                    <table class="table table-striped">
                      <thead>
                        <tr>
                          <th>Deskripsi</th>
                          <th>Jenis</th>
                          <th>Berat</th>
                          <th>File</th>
                          <th>Petugas</th>
                        </tr>
                      </thead>
                      <tbody>
                        <tr>
                          <td><?php echo ($detail[0]->description);?></td>
                          <td><?php echo ($detail[0]->jenis);?></td>
                          <td><?php echo ($detail[0]->weight);?> kg</td>
                          <td><a href="<?php echo ($detail[0]->attachment);?>">Download</a></td>
                          <td>
                            <select class="dropdown">
                             
                                <option>-- Pilih Petugas --</option>
                          <?php 
                          if(count($petugas)>0){
                                $i=0;
                                foreach ($petugas as $k) {
                                  echo '<option value="'.$k->email_user.'">'.$k->name.'</option>';
                                  $i++;
                                }
                          }
                        ?>
                               
                             
                          </select>

                          </td>
                        </tr>
                        
                      </tbody>
                    </table>
                  </div>
                </div>
                <div class="row hidden-print mt-20">
                  <div class="col-xs-12 text-right"><a class="btn btn-primary" href="#" >Tugaskan</a></div>
                </div>
              </section>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <script type="text/javascript">$('#sampleTable').DataTable();</script>