package com.budgester.mymarina;

/**
 * Created by budgester on 17/09/13.
 */
public class Marina {

    String _marina_name;
    String _main_code;
    String _pontoon_code;
    String _male_toilet;
    String _male_shower;
    String _female_toilet;
    String _female_shower;

    public Marina(){
    }

    public Marina(String marina_name){
        this._marina_name = marina_name;
    }

    //Marina Name
    public String get_marina_name(){return _marina_name;}
    public void set_marina_name(String marina_name){this._marina_name = marina_name;}

    //Main Code
    public String get_main_code(){return _main_code;}
    public void set_main_code(String main_code){this._main_code = main_code;}

    //Pontoon Code
    public String get_pontoon_code(){return _pontoon_code;}
    public void set_pontoon_code(String pontoon_code){this._pontoon_code = pontoon_code;}

    //Male Toilet
    public String get_male_toilet(){return this._male_toilet;}
    public void set_male_toilet(String male_toilet){this._male_toilet = male_toilet;}

    //Male Shower
    public String get_male_shower(){return this._male_shower;}
    public void set_male_shower(String male_shower){this._male_shower = male_shower;}

    //Female Toilet
    public String get_female_toilet(){return this._female_toilet;}
    public void set_female_toilet(String female_toilet){this._female_toilet = female_toilet;}

    //Female Shower
    public String get_female_shower(){return this._female_shower;}
    public void set_female_shower(String female_shower){this._female_shower = female_shower;}
}