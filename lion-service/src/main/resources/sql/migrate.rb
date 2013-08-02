#!/usr/bin/ruby

require "rubygems"
require "mysql"

project = "group-service"
if ARGV.size > 0
  project = ARGV[0]
end

def get_project_id project
  result = $lion_db.query("select id from project where name = '#{project}'")
  row = result.fetch_row
  return row[0]
end

def insert_lion_db (project, service)
  puts "Start to migrate #{project} #{service['serviceName']}"
  project_id = get_project_id project
  for env  in (1..5)
    field = "serviceIp" + env.to_s;
    if service[field] != nil
      sql = "insert into service values (NULL, #{project_id}, #{env}, '#{service['serviceName']}', '#{service['des']}', '', '#{service[field]}')"
      puts sql
      $lion_db.query(sql)
    end
  end
  puts "DONE"
end

def migrate project
  $lion_db = Mysql.real_connect("192.168.8.44", "lion", "dp!@ZUFMehRDV", "lion")
  $lion_db.query("set names utf8")
  puts "Connected to lion.dp DB: " + $lion_db.get_server_info

  db = Mysql.real_connect("192.168.7.105", "dpcom_hawk", "123456", "hawk")
  db.query("set names utf8")
  puts "Connected to jm.dp DB: " + db.get_server_info

  result = db.query("select * from service where projectId = (select id from project where name='#{project}')")
  result.each_hash do |service|
    insert_lion_db project, service
  end

  db.close
  $lion_db.close
end

migrate project
