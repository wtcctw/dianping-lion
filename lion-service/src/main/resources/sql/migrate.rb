#!/usr/bin/ruby

require "rubygems"
require "mysql"

def get_project_id project
  result = $lion_db.query("select id from project where name = '#{project}'")
  row = result.fetch_row
  return row != nil ? row[0] : nil
end

def insert_lion_db (project, service)
  # puts "Start to migrate #{project} #{service['serviceName']}"
  project_id = get_project_id project
  for env  in (1..3)
  #!! for env  in (4..5)
    field = "serviceIp" + env.to_s;
    #!! field = "serviceip" + env.to_s;
    if service[field] != nil
      sql = "insert into service values (NULL, #{project_id}, #{env}, '#{service['serviceName']}', '#{service['des']}', '', '#{service[field]}')"
      # puts sql
      begin
        $lion_db.query(sql)
      rescue
        puts "#{$!}"
      end
    end
  end
  # puts "DONE"
end

def migrate project
  project_id = get_project_id project
  if(project_id == nil)
    puts "No matching project #{project}"
    return
  end

  result = $jm_db.query("select * from service where projectId = (select id from project where name='#{project}')")
  result.each_hash do |service|
    insert_lion_db project, service
  end
end

def migrate_all
  result = $jm_db.query("select * from project where id in (select distinct projectId from service)")
  result.each_hash do |project|
    migrate project["name"]
  end
end

#$lion_db = Mysql.real_connect("192.168.8.44", "lion", "dp!@ZUFMehRDV", "lion") # new.lion.beta
$lion_db = Mysql.real_connect("10.1.1.220", "lion", "dp!@tempuser", "lion") # new.lion.online
$lion_db.query("set names utf8")

$jm_db = Mysql.real_connect("192.168.7.105", "dpcom_hawk", "123456", "hawk") # old.lion.beta
#!! $jm_db = Mysql.real_connect("10.1.1.220", "hawk", "dp!@tuUAVZZpD", "hawk") # old.lion.online
$jm_db.query("set names utf8")

if ARGV.size > 0
  migrate ARGV[0]
else
  migrate_all
end

$lion_db.close
$jm_db.close
