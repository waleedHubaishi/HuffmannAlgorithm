/**
 * Created by Hubaishi on 01.06.17.
 */
public class Node{

    //the node entity class, its code, character, probabilty, left and right node
        float Proberty;
        String data;
        Node right;
        Node left;
        String code;
        public String getData() {
            return data;
        }

        public Node(String data_,float proberty_,Node right_,Node left_) {
            data=data_;
            Proberty=proberty_;
            right=right_;
            left=left_;
            code="";

        }

        public String  getCode() {
            return code;
        }
        public void setCode(String code) {
            this.code=code;
        }
        public float getProberty() {
            return Proberty;
        }
        public Node getright() {
            return right;
        }
        public Node getLeft() {
            return left;
        }

}
