package fast.cloud.nacos.mybatis.service.impl;


public class ShowMeBug {

    static class ListNode {
        int val;
        ListNode next;

        ListNode(int x) {
            val = x;
        }

        public void addNode(ListNode addNode) {
            //new一个节点,也就是要添加的节点
            if (next == null) {
                next = addNode;
            }
            //不能直接对头结点操作,头结点是我们找到这个链表的头,要一直拿在手里
            //new 一个节点对象,做一个游动变量
            else {
                ListNode temp = next;
                while (temp.next != null) {
                    temp = temp.next;
                }
                temp.next = addNode;
            }
        }
    }

    public static ListNode mergeTwoLists(ListNode l1, ListNode l2) {
        // 请在此编辑你的代码
        ListNode cur1 = l1;
        ListNode cur2 = l2;
        ListNode result = null;
        ListNode last = null;
        if (cur1 == null) {
            return cur2;
        }
        if (cur2 == null) {
            return cur1;
        }
        while (cur1 != null && cur2 != null) {
            if (cur1.val <= cur2.val) {
                ListNode next = cur1.next;
                cur1.next = null;
                if (result == null) {
                    result = cur1;
                } else {
                    last.next = cur1;
                }
                last = cur1;
                cur1 = next;
            } else {
                ListNode next = cur2.next;
                cur2.next = null;
                if (result == null) {
                    result = cur2;
                } else {
                    last.next = cur2;
                }
                last = cur2;
                cur2 = next;
            }
        }
        if (cur1 != null) {
            last.next = cur1;
        }
        if (cur2 != null) {
            last.next = cur2;
        }
        return result;
    }


    public static void main(String[] args) {
        System.out.println("开始测试");
        java.util.Scanner in = new java.util.Scanner(System.in);
        String str = in.nextLine();
        String[] split = str.split(", ");

        String[] arr1 = split[0].split("->");
        String[] arr2 = split[1].split("->");
        ListNode listNode = new ListNode(Integer.parseInt(arr1[0]));
        for (int i = 1; i < arr1.length; i++) {
            listNode.addNode(new ListNode(Integer.parseInt(arr1[i])));
        }

        ListNode listNode2 = new ListNode(Integer.parseInt(arr2[0]));
        for (int i = 1; i < arr2.length; i++) {
            listNode2.addNode(new ListNode(Integer.parseInt(arr2[i])));
        }

        ListNode resultNode = mergeTwoLists(listNode, listNode2);
        String result = resultNode.val + "->";
        while (resultNode != null) {
            result += resultNode.val + "->";
            resultNode = resultNode.next;
        }
        System.out.println(result.substring(0,result.length()-2));
        // 请在此编辑你的测试代码
    }
}

