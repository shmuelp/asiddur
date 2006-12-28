package avrom.util;
import java.util.*;

/** 
* The RegularHebrewDate class is a subclass of the HebrewDate class that adds the following 
* functionality to it.<P>
* <UL>
* <LI>Getting the corresponding jewish holiday or fast day. (no "modern" holidays)
* <LI>Getting the regular parsha (Torah or sedra) reading for Shabbos.
* <LI>Getting the current Omer count.
* </UL>
* In addition, This class has settings for Ashkenazi or Sephardi pronunciations and
* whether the parsha and holiday scheme follows the Israel scheme or outside Israel scheme.
* <P>
* The methods used to obtain the parsha were derived from 
* the source code of hebcal by Danny Sadinoff and JCal for the Mac 
* by Frank Yellin. Both based their code on routines by Nachum
* Dershowitz and Edward M. Reingold.

* hebcal web page is at <A HREF="http://www.sadinoff.com/hebcal/">www.sadinoff.com/hebcal</A>
*/
public class RegularHebrewDate extends HebrewDate
{
	private boolean ashkenaz= true;
	private boolean israeli= false;
	
	// parsha names in both ashkenazi and sephardi pronunciation
	// Somewhat redundant (don't you think?) 
	private static final String[][] parshios=
	{
	  {"Bereshit", "Bereshis"},
	  {"Noach", "Noach"},
	  {"Lech-Lecha", "Lech-Lecha"},
	  {"Vayera", "Vayera"},
	  {"Chayei Sara", "Chayei Sara"},
	  {"Toldot", "Toldos"},
	  {"Vayetzei", "Vayetzei"},
	  {"Vayishlach", "Vayishlach"},
	  {"Vayeshev", "Vayeshev"},
	  {"Miketz", "Miketz"},
	  {"Vayigash", "Vayigash"},
	  {"Vayechi", "Vayechi"},
	  {"Shemot", "Shemos"},
	  {"Vaera", "Vaera"},
	  {"Bo", "Bo"},
	  {"Beshalach", "Beshalach"},
	  {"Yitro", "Yisro"},
	  {"Mishpatim", "Mishpatim"},
	  {"Terumah", "Terumah"},
	  {"Tetzaveh", "Tetzaveh"},
	  {"Ki Tisa", "Ki Sisa"},
	  {"Vayakhel", "Vayakhel"},
	  {"Pekudei", "Pekudei"},
	  {"Vayikra", "Vayikra"},
	  {"Tzav", "Tzav"},
	  {"Shmini", "Shmini"},
	  {"Tazria", "Sazria"},
	  {"Metzora", "Metzora"},
	  {"Achrei Mot", "Achrei Mos"},
	  {"Kedoshim", "Kedoshim"},
	  {"Emor", "Emor"},
	  {"Behar", "Behar"},
	  {"Bechukotai", "Bechukosai"},
	  {"Bamidbar", "Bamidbar"},
	  {"Nasso", "Nasso"},
	  {"Beha'alotcha", "Beha'aloscha"},
	  {"Sh'lach", "Sh'lach"},
	  {"Korach", "Korach"},
	  {"Chukat", "Chukas"},
	  {"Balak", "Balak"},
	  {"Pinchas", "Pinchas"},
	  {"Matot", "Matos"},
	  {"Masei", "Masei"},
	  {"Devarim", "Devarim"},
	  {"Vaetchanan", "Vaeschanan"},
	  {"Eikev", "Eikev"},
	  {"Re'eh", "Re'eh"},
	  {"Shoftim", "Shoftim"},
	  {"Ki Teitzei", "Ki Seitzei"},
	  {"Ki Tavo", "Ki Savo"},
	  {"Nitzavim", "Nitzavim"},
	  {"Vayeilech", "Vayeilech"},
	  {"Ha'Azinu", "Ha'Azinu"},
	  {"Vayakhel Pekudei", "Vayakhel Pekudei"},
	  {"Tazria Metzora", "Sazria Metzora"},
	  {"Achrei Mot Kedoshim", "Achrei Mos Kedoshim"},
	  {"Behar Bechukotai", "Behar Bechukosai"},
	  {"Chukat Balak", "Chukas Balak"},
	  {"Matot Masei", "Matos Masei"},
	  {"Nitzavim Vayeilech", "Nitzavim Vayeilech"}
	};

	// These indices were originaly included in the emacs 19 distribution. 
	// These arrays determine the correct indices into the parsha names     
	// -1 means no parsha that week, values > 52 means it is a double parsha

	private static final int [] Sat_short=	
	 {  -1,  52,  -1,  -1,   0,   1,   2,   3,   4,   5,   6,   7,   8,   9,  10,
	    11,  12,  13,  14,  15,  16,  17,  18,  19,  20, 53,  23,  24,  -1,  25,
	   54, 55,  30, 56,  33,  34,  35,  36,  37,  38,  39,  40, 58,  43,  44,
	    45,  46,  47,  48,  49,  50};

	private static final int [] Sat_long=
	 {  -1,  52,  -1,  -1,   0,   1,   2,   3,   4,   5,   6,   7,   8,   9,  10,
	    11,  12,  13,  14,  15,  16,  17,  18,  19,  20, 53,  23,  24,  -1,  25,
	   54, 55,  30, 56,  33,  34,  35,  36,  37,  38,  39,  40, 58,  43,  44,
	    45,  46,  47,  48,  49, 59};

	private static final int [] Mon_short= 
	 {  51,  52,  -1,   0,   1,   2,   3,   4,   5,   6,   7,   8,   9,  10,  11,
	    12,  13,  14,  15,  16,  17,  18,  19,  20, 53,  23,  24,  -1,  25, 54,
	   55,  30, 56,  33,  34,  35,  36,  37,  38,  39,  40, 58,  43,  44,  45,
	    46,  47,  48,  49, 59};

	private static final int [] Mon_long= // split //
	 {  51,  52,  -1,   0,   1,   2,   3,   4,   5,   6,   7,   8,   9,  10,  11,
	    12,  13,  14,  15,  16,  17,  18,  19,  20, 53,  23,  24,  -1,  25, 54,
	   55,  30, 56,  33,  -1,  34,  35,  36,  37, 57,  40, 58,  43,  44,  45,
	    46,  47,  48,  49, 59};


	private static final int [] Thu_normal=
	 {  52,  -1,  -1,   0,   1,   2,   3,   4,   5,   6,   7,   8,   9,  10,  11,
	    12,  13,  14,  15,  16,  17,  18,  19,  20, 53,  23,  24,  -1,  -1,  25,
	   54, 55,  30, 56,  33,  34,  35,  36,  37,  38,  39,  40, 58,  43,  44,
	    45,  46,  47,  48,  49,  50};
	private static final int [] Thu_normal_Israel= 
	 {  52,  -1,  -1,   0,   1,   2,   3,   4,   5,   6,   7,   8,   9,  10,  11,
	    12,  13,  14,  15,  16,  17,  18,  19,  20, 53,  23,  24,  -1,  25, 54,
	   55,  30,  31,  32,  33,  34,  35,  36,  37,  38,  39,  40, 58,  43,  44,
	    45,  46,  47,  48,  49,  50};

	private static final int [] Thu_long= 
	 {  52,  -1,  -1,   0,   1,   2,   3,   4,   5,   6,   7,   8,   9,  10,  11,
	    12,  13,  14,  15,  16,  17,  18,  19,  20,  21,  22,  23,  24,  -1,  25,
	   54, 55,  30, 56,  33,  34,  35,  36,  37,  38,  39,  40, 58,  43,  44,
	    45,  46,  47,  48,  49,  50};

	private static final int [] Sat_short_leap=
	 {  -1,  52,  -1,  -1,   0,   1,   2,   3,   4,   5,   6,   7,   8,   9,  10,
	    11,  12,  13,  14,  15,  16,  17,  18,  19,  20,  21,  22,  23,  24,  25,
	    26,  27,  -1,  28,  29,  30,  31,  32,  33,  34,  35,  36,  37,  38,  39,
	    40, 58,  43,  44,  45,  46,  47,  48,  49, 59};

	private static final int [] Sat_long_leap= 
	 {  -1,  52,  -1,  -1,   0,   1,   2,   3,   4,   5,   6,   7,   8,   9,  10,
	    11,  12,  13,  14,  15,  16,  17,  18,  19,  20,  21,  22,  23,  24,  25,
	    26,  27,  -1,  28,  29,  30,  31,  32,  33,  -1,  34,  35,  36,  37, 57,
	    40, 58,  43,  44,  45,  46,  47,  48,  49, 59};

	private static final int [] Mon_short_leap= 
	 {  51,  52,  -1,   0,   1,   2,   3,   4,   5,   6,   7,   8,   9,  10,  11,
	    12,  13,  14,  15,  16,  17,  18,  19,  20,  21,  22,  23,  24,  25,  26,
	    27,  -1,  28,  29,  30,  31,  32,  33,  -1,  34,  35,  36,  37, 57,  40,
	   58,  43,  44,  45,  46,  47,  48,  49, 59};
	private static final int [] Mon_short_leap_Israel= 
	 {  51,  52,  -1,   0,   1,   2,   3,   4,   5,   6,   7,   8,   9,  10,  11,
	    12,  13,  14,  15,  16,  17,  18,  19,  20,  21,  22,  23,  24,  25,  26,
	    27,  -1,  28,  29,  30,  31,  32,  33,  34,  35,  36,  37,  38,  39,  40,
	   58,  43,  44,  45,  46,  47,  48,  49, 59};

	private static final int [] Mon_long_leap= 
	 {  51,  52,  -1,   0,   1,   2,   3,   4,   5,   6,   7,   8,   9,  10,  11,
	    12,  13,  14,  15,  16,  17,  18,  19,  20,  21,  22,  23,  24,  25,  26,
	    27,  -1,  -1,  28,  29,  30,  31,  32,  33,  34,  35,  36,  37,  38,  39,
	    40, 58,  43,  44,  45,  46,  47,  48,  49,  50};
	private static final int [] Mon_long_leap_Israel= 
	 {  51,  52,  -1,   0,   1,   2,   3,   4,   5,   6,   7,   8,   9,  10,  11,
	    12,  13,  14,  15,  16,  17,  18,  19,  20,  21,  22,  23,  24,  25,  26,
	    27,  -1,  28,  29,  30,  31,  32,  33,  34,  35,  36,  37,  38,  39,  40,
	    41,  42,  43,  44,  45,  46,  47,  48,  49,  50};

	private static final int [] Thu_short_leap=
	 {  52,  -1,  -1,   0,   1,   2,   3,   4,   5,   6,   7,   8,   9,  10,  11,
	    12,  13,  14,  15,  16,  17,  18,  19,  20,  21,  22,  23,  24,  25,  26,
	    27,  28,  -1,  29,  30,  31,  32,  33,  34,  35,  36,  37,  38,  39,  40,
	    41,  42,  43,  44,  45,  46,  47,  48,  49,  50};
	 
	private static final int[] Thu_long_leap = 
	 {  52,  -1,  -1,   0,   1,   2,   3,   4,   5,   6,   7,   8,   9,  10,  11,
	    12,  13,  14,  15,  16,  17,  18,  19,  20,  21,  22,  23,  24,  25,  26,
	    27,  28,  -1,  29,  30,  31,  32,  33,  34,  35,  36,  37,  38,  39,  40,
	    41,  42,  43,  44,  45,  46,  47,  48,  49, 59};

	/** Initializes based on a month, date, year and whether it is ashkenaz pronunciation
	* or whether it is Israel parsha scheme. */ 
	public RegularHebrewDate (int m, int d, int y, boolean ashkenaz, boolean israeli) throws HebrewDateException
	{
		super(m, d, y);
		this.ashkenaz= ashkenaz;
		this.israeli= israeli;
	}
	/** Initializes based on a month, date, year */
	public RegularHebrewDate(int m, int d, int y) throws HebrewDateException
	{
		super(m,d,y);
	}
	/**
	* Initializes a date based on the current system date. 
	*/
	public RegularHebrewDate()
	{
		super();
	}
	/**
	* Initializes a date based on java.util.Date object.
	*/

	public RegularHebrewDate(Date date)
	{
		super(date);
	}
	/**
	* Initializes a date based on java.util.Calendar object.
	*/

	public RegularHebrewDate(Calendar cal)
	{
		super(cal);
	}
	/**
	* Sets whether to use Ashkenazi pronunciation or not. Default is "true".
	*/
	public void setAshkenaz(boolean ashkenaz)
	{
		this.ashkenaz= ashkenaz;
	}
	/**
	* Gets the Ashkenazi pronunciation settings. Default is "true".
	*/
	public boolean isAshkenaz()
	{
		return ashkenaz;
	}
	/**
	* Sets whether to use Israel parsha and holiday scheme or not. Default is "false".
	*/
	public void setIsraeli(boolean israeli)
	{
		this.israeli= israeli;
	}
	/**
	* Gets whether Israel parsha and holiday scheme is used or not. Default is "false".
	*/
	public boolean isIsraeli()
	{
		return israeli;
	}
	/** 
	* returns a String of the holiday or fast day for the current day, or an empty string
	* if there is no holiday for this day. Has no "modern" holidays. 
	* @return A String containing the holiday name or an empty string.
	*/
	public String getHoliday ()
	{
	// check by month (starts from Nissan)
	switch (hebrewMonth)
		{
		case 1:
			if (hebrewDate==14)
				return "Erev Pesach";

			if (hebrewDate== 15 || hebrewDate== 21 || (!israeli && (hebrewDate == 16 || hebrewDate== 22)) )
					return "Pesach";

			if (hebrewDate>=17 && hebrewDate <=20 || (hebrewDate==16 && israeli))
				return "Chol Hamoed Pesach";
		break;
		case 2:
			if (hebrewDate== 14)
				return "Pesach Sheni";
		break;
		case 3:
			if (hebrewDate== 5)
			{
				if (ashkenaz)
					return "Erev Shavuos";
				else
					return "Erev Shavuot";
			}
			if (hebrewDate== 6 || (hebrewDate== 7 && !israeli))
			{
				if (ashkenaz)
					return "Shavuos";
				else
					return "Shavuot";
			}
		break;
		case 4:
			// push off the fast day if it falls on shabbos//
			if ((hebrewDate== 17 && day!=7) || (hebrewDate==18 && day==1))
				return "Tzom Tammuz";
		break;
		case 5:
			// if tisha b'av falls on shabbos, push off until sunday//
			if ((day== 1 && hebrewDate== 10) || (day != 7 && hebrewDate==9))
				return "Tisha B'av";
			if (hebrewDate== 15)
				return "Tu B'Av";
		break;
		case 6:
			if (hebrewDate== 29)
				return "Erev Rosh Hashanah";
		break;
		case 7:
			if (hebrewDate== 1 || hebrewDate== 2)
				return "Rosh Hashanah";
			// push off tzom gedalia if it falls on Shabbos //
			if ((hebrewDate== 3 && day != 7) || (hebrewDate== 4 && day == 1))
				return "Tzom Gedalia";
			if (hebrewDate== 9)
				return "Erev Yom Kippur";
			if (hebrewDate== 10)
				return "Yom Kippur";
			if (hebrewDate== 14)
				{
				if (ashkenaz)
					return "Erev Sukkos";
				else
					return "Erev Sukkot";
				}
			if (hebrewDate== 15 || (hebrewDate== 16 && !israeli))
				{
				if (ashkenaz)
					return "Sukkos";
				else
					return "Sukkot";
				}
			if (hebrewDate >=17 && hebrewDate <= 20 || (hebrewDate== 16 && israeli))
				{
				if (ashkenaz)
					return "Chol Hamoed Sukkos";
				else
					return "Chol Hamoed Sukkot";
				}
			if (hebrewDate== 21)
				return "Hoshana Rabah";
			if (hebrewDate== 22)
				{
				if (ashkenaz)
					return "Shmini Atzeres";
				else
					return "Shmini Atzeret";
				}
			if (hebrewDate== 23 && !israeli)
				{
				if (ashkenaz)
					return "Simchas Torah";
				else
					return "Simchat Torah";
				}
		break;
		case 9:
			if (hebrewDate== 24)
				return "Erev Chanukah";
			if (hebrewDate>= 25)
				return "Chanukah";
		break;
		case 10:
			if (hebrewDate== 1 || hebrewDate== 2 || (hebrewDate==3 && isKislevShort()) )
				return "Chanukah";
			if (hebrewDate== 10)
				{
				if (ashkenaz)
					return "Tzom Teves";
				else
					return "Tzom Tevet";
				}
		break;
		case 11:
			if (hebrewDate== 15)
				return "Tu B'Shvat";
		break;
		case 12:
			if (!isHebrewLeapYear())
				{
				// if 13th adar falls on fri or shabbos, push back to thursday
				if ( ((hebrewDate== 11 || hebrewDate== 12) && day== 5) ||
				(hebrewDate== 13 && !(day==6 || day==7)) )
					{
					if (ashkenaz)
						return "Ta'anis Esther";
					else
						return "Ta'anit Esther";
					}
				if (hebrewDate== 14)
					return "Purim";
				if (hebrewDate== 15)
					return "Shushan Purim";
				}
				// else if a leap year //
			else
				{
				if (hebrewDate== 14)
					return "Purim Katan";
				}
		break;
		case 13:
				// if 13th adar falls on fri or shabbos, push back to thursday
				if ( ((hebrewDate== 11 || hebrewDate== 12) && day== 5) ||
				(hebrewDate== 13 && !(day==6 || day==7)) )
					{
					if (ashkenaz)
						return "Ta'anis Esther";
					else
						return "Ta'anit Esther";
					}
			if (hebrewDate== 14)
				return "Purim";
			if (hebrewDate== 15)
				return "Shushan Purim";
		break;
		}
	// if we get to this stage, then there are no holidays for the given date 
	return "";
	}

	/** returns the int value of the omer day or zero if the day is not in the omer 
	* @return The omer count as an int or zero. */
	public int getOmer()
	{
	int omer= 0;

	// if nissan and second day of pesach and on //
	if (hebrewMonth== 1 && hebrewDate >= 16)
		omer= hebrewDate - 15;
	// if iyar //
	if (hebrewMonth== 2)
		omer= hebrewDate + 15;
	// if sivan and before shavuos //
	if (hebrewMonth== 3 && hebrewDate < 6)
		omer= hebrewDate + 44;
	
	return omer;
	}

	/** returns a String of the omer day in the form "Omer X" or "Lag B'Omer"
	* or an empty string if there is no omer this day */
	public String getOmerAsString()
	{
		int omer= getOmer();

		// if not omer day //
		if (omer == 0)
			return "";
		// if lag b'omer //
		else if (omer == 33)
			return "Lag B'Omer";
		else
			return "Omer " + omer;
		
	}

	/** returns a string of today's parsha(ios) or an empty string if there are none */
	public String getParsha()
	{
	
	// if today is not shabbos, then there is no normal parsha reading 
	if (getDayOfWeek() != 7)
		return "";
	
	// kvia= whether a hebrew year is short/regular/long (0/1/2) 
	// roshHashana= Rosh Hashana of this hebrew year
	// roshHashanaDay= day of week Rosh Hashana was on this year
	// week= current week in hebrew calendar from Rosh Hashana
	// array= the correct index array for this hebrew year
	// index= the index number of the parsha name
	int kvia;
	int roshHashanaDay;
	int week;
	int[] array= null;
	int index;
	// create a clone of this date
	HebrewDate roshHashana= (HebrewDate) this.clone();
	try
	{
		// set it to Rosh Hashana of this year 
		roshHashana.setHebrewDate(7, 1, CURRENT_YEAR);
	}
	catch (HebrewDateException e)
	{
		e.printStackTrace();
	}
		
	// get day Rosh Hashana was on 
	roshHashanaDay= roshHashana.getDayOfWeek();
	
	// week is the week since the first Shabbos on or after Rosh Hashana 
	week= ( ((absDate- roshHashana.getAbsDate()) - (7- roshHashanaDay)) /7);
	
	// get kvia 
	if (isCheshvanLong() && !isKislevShort())
		kvia= 2;
	else if (! isCheshvanLong() && isKislevShort())
		kvia= 0;
	else
		kvia= 1;

	// determine appropriate array 
	if (!isHebrewLeapYear())
		{
		switch (roshHashanaDay)
			{
			case 7:  // RH was on a Saturday 
				if (kvia == 0)       array = Sat_short;
				else if (kvia == 2)  array = Sat_long;
				break;
			case 2:  // RH was on a Monday 
				if (kvia == 0)       array = Mon_short;
				else if (kvia == 2)  array = israeli ? Mon_short : Mon_long;
				break;
			case 3:  // RH was on a Tuesday 
				if (kvia == 1) 	 	 array = israeli ? Mon_short : Mon_long;
				break;
			case 5:  // RH was on a Thursday 
				if (kvia == 1)		array = israeli ? Thu_normal_Israel : Thu_normal;
				else if (kvia == 2) array = Thu_long;
				break;
			}
		} 
		
	// if leap year //
	else 
		{
		switch (roshHashanaDay)
			{
			case 7:  // RH was on a Sat 
				if (kvia == 0)      array = Sat_short_leap;
				else if (kvia == 2) array = israeli ? Sat_short_leap : Sat_long_leap;
				break;
			case 2:  // RH was on a Mon 
				if (kvia == 0)      array = israeli ? Mon_short_leap_Israel : Mon_short_leap;
				else if (kvia == 2) array = israeli ? Mon_long_leap_Israel : Mon_long_leap;
				break;
			case 3:  // RH was on a Tue 
				if (kvia == 1)      array = israeli ? Mon_long_leap_Israel : Mon_long_leap;
				break;
			case 5:  // RH was on a Thu 
				if (kvia == 0)      array = Thu_short_leap;
				else if (kvia == 2) array = Thu_long_leap;
				break;
			}
		}
	// if something goes wrong 
	if (array == null)
		throw new RuntimeException("Was not able to set the index array to any of the known types.");
	
	// get index from array
	index= array[week];
	
	// If no Parsha this week 
	if (index == -1) 				
		return "";

	// if parsha this week
	else
	{
		int subindex;
		if (ashkenaz)
			subindex= 1;
		else
			subindex= 0;
		return parshios[index][subindex];
	
	}
	}
	/** Create a copy of this date. */
	public Object clone()
	{
		try
		{
			return new RegularHebrewDate(month, date, year, ashkenaz, israeli);
		}
		catch (HebrewDateException e)
		{
			e.printStackTrace();
			return null;
		}

	}
	/*
	public static void main(String[] args)
	{
		try
		{
			 HebrewDate date= new HebrewDate();
			 date.setHebrewDate(7, 10, 5660);
			 
			 for (int i=1900; i < 2000; i++)
			 {
			 	
			 	if (date.getDayOfWeek() == 7)
			 	{
			 		System.out.println(date.getDateAsString());
			 	}
			 	date.setHebrewDate(HebrewDate.CURRENT_MONTH, HebrewDate.CURRENT_DATE, date.getHebrewYear() + 1);
			 }
			 }
			 catch (Exception e)
			 {
			 	e.printStackTrace();
			 }
		}*/
}
