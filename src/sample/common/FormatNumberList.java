package sample.common;

import sample.P;

import java.util.*;

/**
 * Created by Mitra on 08/09/18.
 */
public class FormatNumberList {
    public static String format1(ArrayList<String> nosArrayList){
        String outPutString = "";

        Set sorted = new TreeSet<Integer>();

        for(int i=0; i<nosArrayList.size(); i++){
            sorted.add(Integer.parseInt(nosArrayList.get(i)));
        }

        Iterator itr = sorted.iterator();
        int i=0;
        while(itr.hasNext()){
            P.p("Data("+(1+i)+") : "+nosArrayList.get(i)+"     Data("+(++i)+") : "+itr.next());
        }

        String str1 = "", str2 = "";

        ArrayList sortedList = new ArrayList<Integer>(sorted);
        for(int ii=0; ii<nosArrayList.size(); ii++){
            P.p("sortedList :"+ sortedList.get(ii));
        }

        int j=0;
        ArrayList kkk = new ArrayList<String>();
        int index = 0;

        int temp=0, first = 0,  last=0;

        for(int kk = 0; kk<sortedList.size(); kk++ ){

            first = (int) sortedList.get(kk);
            temp = first;
            if( (kk+1)<sortedList.size() && temp == ((int) sortedList.get(kk+1) - 1)) {
                P.p("temp : "+temp +"  ==  "+sortedList.get(kk));
                kk++;
                while ( (kk<sortedList.size()) && temp == ((int) sortedList.get(kk) - 1)) {
                    P.p("temp : "+temp +"  ==  "+sortedList.get(kk));
                    temp = ((int) sortedList.get(kk));
                    last = temp;
                    kk++;
                }
            }
            if(first>0 && last>0){
                kkk.add(first+"-"+last);
            }else if(first>0 && last==0){
                kkk.add(first);
            }

        }


        /*for(int kk = 0; kk<sortedList.size(); kk++ ){
            P.p("1 str1 : "+str1);
            if(str1.length()<=0) {
                str1 = String.valueOf(sortedList.get(kk));
                P.p("2 str1 : "+str1);
            }else{
                while(true) {
                    P.p("3 str1 : "+str1);
                    if ((int) sortedList.get(kk) == ((int) sortedList.get(kk-1))) {
                        P.p("4 str2 : "+str2);
                        str2 = String.valueOf(sortedList.get(kk));
                        P.p("5 str : "+str2);
                    }else{
                        break;
                    }
                }
                if(!str2.isEmpty()){
                    kkk.add(str1);
                }else{
                    kkk.add(str1+"-"+str2);
                }
                str1=str2="";
            }
        }*/



        return outPutString = kkk.toString() ;
    }
}
