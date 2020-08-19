package com.example.rpcprovider.cannaldemo.demo;

/**
 * @ClassName ClientSample
 * @Author kris
 * @Date 2020/6/8
 **/

import com.alibaba.otter.canal.client.CanalConnector;
import com.alibaba.otter.canal.client.CanalConnectors;
import com.alibaba.otter.canal.common.utils.AddressUtils;
import com.alibaba.otter.canal.protocol.CanalEntry;
import com.alibaba.otter.canal.protocol.CanalEntry.Column;
import com.alibaba.otter.canal.protocol.Message;

import com.alibaba.otter.canal.protocol.CanalEntry.Entry;
import com.example.utils.Utils;

import java.net.InetSocketAddress;
import java.nio.channels.ClosedByInterruptException;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ClientSample {
    public static int batchSize = 1000;
    public static void main(String args[]) {
//        int batchSize = 1000;
        // 创建链接
//        CanalConnector connector = CanalConnectors.newSingleConnector(new InetSocketAddress(AddressUtils.getHostIp(),
//                11111), "example", "", "");
        String zkServers = "127.0.0.1:2181,127.0.0.1:2182,127.0.0.1:2183";
        String destination ="example";
        String tables = "credit_record";
        CanalConnector connector = CanalConnectors.newClusterConnector(zkServers, "example", "", "");
        int emptyCount = 0;
        try {
            connector.connect();
            connector.subscribe(".*\\..*");
            connector.rollback();
            int totalEmtryCount = 1200;
            while (emptyCount < totalEmtryCount) {
                Message message = connector.getWithoutAck(batchSize); // 获取指定数量的数据
                long batchId = message.getId();
                int size = message.getEntries().size();
                if (batchId == -1 || size == 0) {
                    emptyCount++;
                    System.out.println("empty count : " + emptyCount);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
                    emptyCount = 0;
                    // System.out.printf("message[batchId=%s,size=%s] \n", batchId, size);
                    printEntry(message.getEntries());
                }

                connector.ack(batchId); // 提交确认
                // connector.rollback(batchId); // 处理失败, 回滚数据
            }

            System.out.println("empty too many times, exit");
        } finally {
            connector.disconnect();
        }

//        try {
//            // 创建链接
//            CanalConnector connector = CanalConnectors.newClusterConnector(zkServers, destination, "", "");
////		    CanalConnector connector = CanalConnectors.newSingleConnector(
////		    		new InetSocketAddress(address, port), destination, "", "");
//            while(true) {
//                System.out.println("创建连接："+destination+"->"+tables);
//                try {// 异常重试
//                    connector.connect();
//                    connector.subscribe(tables);
////			        connector.subscribe("aaa.counter,aaa.t_obd_terminal,aaa.test");
//                    connector.rollback();
//                    readData(connector);
//                } catch (Exception e) {
//                    if(!Utils.existThrowable(e, ClosedByInterruptException.class) &&
//                            !Utils.existThrowable(e, InterruptedException.class)) {
//                        System.out.println("同步数据异常等待重新建立连接：" + destination+ e);
//                    }
//                } finally {
//                    try {
//                        connector.disconnect();
//                    } catch (Exception e1) {
//                        System.out.println("关闭连接异常：" + e1.getMessage()+ e1);
//                    }
//                }
//            }
//        } catch (Exception e) {
//            System.out.println("canal同步异常"+ e);
//        } finally {
//            System.out.println("停止同步：{}"+ destination);
//        }

    }

    /**
     * 读取数据，并ack
     * @param connector
     */
    public static void readData(CanalConnector connector) {
        while (true) {
//			log.info("拉取数据");
            Message message = connector.getWithoutAck(batchSize, 1L, TimeUnit.SECONDS); // 获取指定数量的数据
            long batchId = message.getId();
            int size = message.getEntries().size();
            if (batchId != -1 && size > 0) {
                //log.warn("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@1.获取的message数量是：" + size);
                printEntry(message.getEntries());
                connector.ack(batchId); // 提交确认，如果不ack则客户端下次连接时，继续从之前的位置开始读
//        		connector.rollback(batchId); // 处理失败, 回滚数据(不需要执行，因为如果不ack，就会在下次连接时重新读取)
            } else {
                System.out.println("不知道啥問題");
            }
        }
    }

    private static void printEntry(List<Entry> entrys) {
        for (Entry entry : entrys) {
            if (entry.getEntryType() == CanalEntry.EntryType.TRANSACTIONBEGIN || entry.getEntryType() == CanalEntry.EntryType.TRANSACTIONEND) {
                continue;
            }

            CanalEntry.RowChange rowChage = null;
            try {
                rowChage = CanalEntry.RowChange.parseFrom(entry.getStoreValue());
            } catch (Exception e) {
                throw new RuntimeException("ERROR ## parser of eromanga-event has an error , data:" + entry.toString(),
                        e);
            }

            CanalEntry.EventType eventType = rowChage.getEventType();
            System.out.println(String.format("================> binlog[%s:%s] , name[%s,%s] , eventType : %s",
                    entry.getHeader().getLogfileName(), entry.getHeader().getLogfileOffset(),
                    entry.getHeader().getSchemaName(), entry.getHeader().getTableName(),
                    eventType));

            for (CanalEntry.RowData rowData : rowChage.getRowDatasList()) {
                if (eventType == CanalEntry.EventType.DELETE) {
                    printColumn(rowData.getBeforeColumnsList());
                } else if (eventType == CanalEntry.EventType.INSERT) {
                    printColumn(rowData.getAfterColumnsList());
                } else {
                    System.out.println("-------> before");
                    printColumn(rowData.getBeforeColumnsList());
                    System.out.println("-------> after");
                    printColumn(rowData.getAfterColumnsList());
                }
            }
        }
    }

    private static void printColumn( List<Column> columns) {
        for (Column column : columns) {
            System.out.println(column.getName() + " : " + column.getValue() + "    update=" + column.getUpdated());
        }
    }
}
