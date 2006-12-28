package avrom.util;
import java.util.*;

/**
* The HebrewDate class allows one to maintain an instance of a Gregorian date along
* with the corresponding hebrew date.<P>
* This class can use the standard Java Date and Calendar classes for setting it, but does
* not subclass these classes or use them internally to any extensive use. This class also does not
* have a concept of a time (which the Date class does). If you are looking for
* a class that implements a hebrew calendar version of the Calendar class, one is available
* from <A HREF="http://oss.software.ibm.com/developerworks/opensource/icu4j/">developerWorks</A> by IBM.
* <P>
* The Java code which is contained in this class was translated from my C++
* code. Some of that C++ code was translated or taken from other C/C++ code
* in "Calendrical Calculations" by Nachum Dershowitz and
* Edward M. Reingold, Software-- Practice & Experience,
* vol. 20, no. 9 (September, 1990), pp. 899- 928.
* <P>
* Available at <A HREF="http://emr.cs.uiuc.edu/~reingold/calendar.ps">http://emr.cs.uiuc.edu/~reingold/calendar.ps</A><BR>
* Original C++ source: <A HREF="http://emr.cs.uiuc.edu/~reingold/calendar.C">http://emr.cs.uiuc.edu/~reingold/calendar.C</A>
* <P>
* @see java.util.Date
* @see java.util.Calendar
*/
public class HebrewDate //implements Comparable, Cloneable
{
	/** Current month (hebrew or gregorian) this object is set to. */
	public static final int CURRENT_MONTH= 0;
	/** Current date of month (hebrew or gregorian) this object is set to. */
	public static final int CURRENT_DATE= 0;
	/** Current year (hebrew or gregorian) this object is set to. */
	public static final int CURRENT_YEAR= 0;
	private static final int HEBREW_EPOCH = -1373429;

	private static final String[] hebrewMonths= {"Nissan", "Iyar",	"Sivan", "Tamuz", "Av", "Elul", "Tishrei", "Cheshvan", "Kislev", "Teves", "Shvat", "Adar", "Adar II"};
	private static final String[] months= {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
	protected int hebrewMonth;
	protected int hebrewDate;
	protected int hebrewYear;

	protected int month;
	protected int date;
	protected int year;

	protected int day;

	protected int absDate;
	// Any method with the mark "ND+ER" means that method was taken
	// from this source with minor modifications.

	/**
	* Gets how many days are in a month. */
	// ND+ER
	public int getLastDayOfMonth(int month)
	{
		switch (month)
		{
			case 2:
				if ((((year % 4) == 0) && ((year % 100) != 0))
					|| ((year % 400) == 0))
					return 29;
				else
					return 28;
			case 4:
			case 6:
			case 9:
			case 11: return 30;
			default: return 31;
		}
	}
	/**
	* Gets how many days are in the current month. */
	// ND+ER
	public int getLastDayOfMonth()
	{
		return getLastDayOfMonth(this.month);
	}
	// ND+ER //
	// Computes the Gregorian date from the absolute date.  //
	private void absDateToDate()
	{
		// Search forward year by year from approximate year//
		year = absDate/366;
		while (absDate >= dateToAbsDate(1,1,year+1))
			year++;
		// Search forward month by month from January//
		month = 1;
		while (absDate > dateToAbsDate(month, getLastDayOfMonth(), year))
			month++;
		date = absDate - dateToAbsDate(month,1,year) + 1;
	}
	/**
	* Returns the absolute date (days on the gregorian since Januray 1, 1)
	*/
	public int getAbsDate()
	{
		return absDate;
	}
	// ND+ER //
	// Computes the absolute date from the Gregorian date. Default is current date.//
	private int dateToAbsDate(int month, int date, int year)
	{

	for (int m = month - 1; m > 0; m--) // days in prior months this year //
		date = date + getLastDayOfMonth(m);
	return
		(date					// days this year //
		 + 365 * (year - 1)	// days in previous years ignoring leap days //
		 + (year - 1)/4		// Julian leap days before this year... //
		 - (year - 1)/100		// ...minus prior century years... //
		 + (year - 1)/400);	// ...plus prior years divisible by 400 //
	}

	/**
	* returns true if the year is an Hebrew leap year. */
	public static boolean isHebrewLeapYear(int year)
	{
		if ((((7 * year) + 1) % 19) < 7)
			return true;
		else
			return false;
	}
	/**
	* returns true if the current hebrew year is a leap year. */
	public boolean isHebrewLeapYear()
	{
		return isHebrewLeapYear(this.hebrewYear);
	}

	/**
	* returns the last month of the Hebrew year. That is, either 12 or 13. */
	public int getLastMonthOfHebrewYear()
	{
		if (isHebrewLeapYear())
			return 13;
		else
			return 12;
	}

	// ND+ER //
	// Number of days elapsed from the Sunday prior to the start of the //
	// Hebrew calendar to the mean conjunction of Tishri of Hebrew year.//
	private int getHebrewCalendarElapsedDays(int year)
	{

		int monthsElapsed =
			(235 * ((year - 1) / 19))			// Months in complete cycles so far.//
			+ (12 * ((year - 1) % 19))			// Regular months in this cycle.//
			+ (7 * ((year - 1) % 19) + 1) / 19; // Leap months this cycle//
		int partsElapsed = 204 + 793 * (monthsElapsed % 1080);
		int hoursElapsed =
			5 + 12 * monthsElapsed + 793 * (monthsElapsed/ 1080)
			+ partsElapsed / 1080;
		int conjunctionDay = 1 + 29 * monthsElapsed + hoursElapsed / 24;
		int conjunctionParts = 1080 * (hoursElapsed % 24) + partsElapsed % 1080;
		int alternativeDay;
		if ((conjunctionParts >= 19440)		 // If new moon is at or after midday,//
			|| (((conjunctionDay % 7) == 2)	 // ...or is on a Tuesday...//
				&& (conjunctionParts >= 9924)	 // at 9 hours, 204 parts or later...//
				&& !isHebrewLeapYear(year))	// ...of a common year,//
			|| (((conjunctionDay % 7) == 1)	 // ...or is on a Monday at...//
				&& (conjunctionParts >= 16789) // 15 hours, 589 parts or later...//
				&& (isHebrewLeapYear(year - 1))))// at the end of a leap year//
			// Then postpone Rosh HaShanah one day//
			alternativeDay = conjunctionDay + 1;
		else
			alternativeDay = conjunctionDay;
		if (((alternativeDay % 7) == 0)// If Rosh HaShanah would occur on Sunday,//
			|| ((alternativeDay % 7) == 3)	 // or Wednesday,//
			|| ((alternativeDay % 7) == 5))	 // or Friday//
			// Then postpone it one (more) day//
			return (1+ alternativeDay);
		else
			return alternativeDay;
	}

	// ND+ER //
	// Number of days in Hebrew year.//
	private int getDaysInHebrewYear()
	{
	return ((getHebrewCalendarElapsedDays(hebrewYear + 1)) -
			(getHebrewCalendarElapsedDays(hebrewYear)));
	}

	// ND+ER //
	// True if Heshvan is long in Hebrew year. //
	protected boolean isCheshvanLong()
	{
	if ((getDaysInHebrewYear() % 10) == 5)
		return true;
	else
		return false;
	}

	// ND+ER //
	// True if Kislev is short in Hebrew year.//
	protected boolean isKislevShort()
	{
	if ((getDaysInHebrewYear() % 10) == 3)
		return true;
	else
		return false;
	}


	/**
	* Returns last day of a hebrew month. */
	public int getLastDayOfHebrewMonth(int month)
	{
		if ((month == 2)
			|| (month == 4)
			|| (month == 6)
			|| ((month == 8) && !(isCheshvanLong()))
			|| ((month == 9) && isKislevShort())
			|| (month == 10)
			|| ((month == 12) && !(isHebrewLeapYear()))
			|| (month == 13))
			return 29;
		else
			return 30;
	}
	/**
	* Returns last day of hebrew month.  */
	public int getLastDayOfHebrewMonth()
	{
		return getLastDayOfHebrewMonth(this.hebrewMonth);
	}
	// ND+ER //
	// Computes the Hebrew date from the absolute date. //
	private void absDateToHebrewDate()
	{
		hebrewYear = (absDate + HEBREW_EPOCH) / 366; // Approximation from below.//
		// Search forward for year from the approximation.//
		while (absDate >= hebrewDateToAbsDate(7,1, hebrewYear+ 1))
			hebrewYear++;

		// Search forward for month from either Tishri or Nisan.//
		if (absDate < hebrewDateToAbsDate(1, 1, hebrewYear))
			hebrewMonth = 7;//Start at Tishri //
		else
			hebrewMonth= 1;//Start at Nisan //

		while (absDate > hebrewDateToAbsDate( hebrewMonth, getLastDayOfHebrewMonth(), hebrewYear))
			hebrewMonth++;

		// Calculate the day by subtraction.//
		hebrewDate = absDate - hebrewDateToAbsDate( hebrewMonth, 1, hebrewYear) + 1;
	}


	// ND+ER //
	// Computes the absolute date of Hebrew date. Default is current hebrew date. //
	private int hebrewDateToAbsDate(int month, int date, int year)
	{
	int m;

	// Before Tishri, so add days in prior months//
	if (month < 7)
		{
		// this year before and after Nisan.//
		for (m=7; m <= getLastMonthOfHebrewYear(); m++)
			date = date + getLastDayOfHebrewMonth(m);

		for (m = 1; m < month ; m++)
			date = date + getLastDayOfHebrewMonth(m);
		}

	// Add days in prior months this year//
	else
		{
		for (m = 7; m < month ; m++)
			date = date + getLastDayOfHebrewMonth(m);
		}

	return (date +
			getHebrewCalendarElapsedDays(year) // Days in prior years.//
			 + HEBREW_EPOCH);		// Days elapsed before absolute date 1.//
	}


	/** Inits date based on gregorian date (month, day, year) */
	public HebrewDate (int month, int date, int year) throws HebrewDateException
	{
		// put in nicer message in exception
		if (month == CURRENT_MONTH || date == CURRENT_DATE || year == CURRENT_YEAR)
			throw new HebrewDateException("Illegal value in constructor.");
		setDate(month, date, year);
	}
	/**
	* Initializes a date based on the current system date.
	*/
	public HebrewDate()
	{
		resetDate();
	}
	/**
	* Initializes a date based on java.util.Date object.
	*/
	public HebrewDate (Date date)
	{
		Calendar cal= Calendar.getInstance();
		cal.setTime(date);
		setDate(cal);
	}
	/**
	* Initializes a date based on java.util.Calendar object.
	*/
	public HebrewDate (Calendar cal)
	{
		setDate(cal);
	}
	/**
	* Sets the date based on a java.util.Calendar object. Modifies the hebrew date as well.
	*/
	public void setDate(Calendar cal)
	{
		this.month= cal.get(cal.MONTH) +1;
		this.date= cal.get(cal.DATE);
		this.year= cal.get(cal.YEAR);

		// init the hebrew date //
		absDate= dateToAbsDate(month, date, year);
		absDateToHebrewDate();

		// set day of week //
		day= Math.abs(absDate%7) + 1;
	}

	/** Sets the Gregorian Date, and updates the hebrew date accordingly */
	public void setDate (int month, int date, int year) throws HebrewDateException
	{
		// precond should be 1->12 anyways, but just in case... //
		if (month > 12 || month < 0)
			throw new HebrewDateException("Illegal value for month= " + month);
		if (date < 0)
			throw new HebrewDateException("Illegal value for date= " + date);
		// make sure date is a valid date for the given month, if not, set to last day of month //
		if (date > getLastDayOfMonth(month))
			date= getLastDayOfMonth(month);
		if (year < 0)
			throw new HebrewDateException("Illegal value for year= "+ year);

		// init  month, date, year //
		if (month != CURRENT_MONTH)
			this.month= month;
		if (date != CURRENT_DATE)
			this.date= date;
		if (year != CURRENT_YEAR)
			this.year= year;

		// init the hebrew date //
		absDate= dateToAbsDate(this.month, this.date, this.year);
		absDateToHebrewDate();

		// set day of week //
		day= Math.abs(absDate%7) + 1;
	}

	/** Sets the Hebrew Date, and updates the gregorian date accordingly */
	public void setHebrewDate (int month, int date, int year) throws HebrewDateException
	{
	if (month < 1 || month > getLastMonthOfHebrewYear())
		throw new HebrewDateException("Illegal value for the hebrew month= "+ month);
	if (date < 0)
			throw new HebrewDateException("Illegal value for hebrew date= " + date);
	// make sure date is valid for this month, otherwise, set to last day of month //
	if ( date > getLastDayOfHebrewMonth(month))
		date= getLastDayOfHebrewMonth(month);

	if (year < 0)
		throw new HebrewDateException("Illegal value for the hebrew year= " + year);

	if (month != CURRENT_MONTH)
		hebrewMonth= month;
	if (date != CURRENT_DATE)
		hebrewDate= date;
	if (year != CURRENT_YEAR)
		hebrewYear= year;

	// reset gregorian date //
	absDate= hebrewDateToAbsDate(hebrewMonth, hebrewDate, hebrewYear);
	absDateToDate();

	// reset day of week //
	day= Math.abs(absDate%7) +1;
	}
	/**
	* Returns this object's date as a java.util.Date object. <B>Note</B>: This class does not
	* have a concept of time.
	* @return The date as a java.util.Date object.
	*/
	public Date getTime()
	{
		Calendar cal= Calendar.getInstance();
		//cal.set(year, month -1, date);
                cal.set(Calendar.YEAR, year);
                cal.set(Calendar.MONTH, month-1);
                cal.set(Calendar.DATE, date);
		return cal.getTime();
	}

	/** Resets this date to the current system date. */
	public void resetDate()
	{
		Calendar cal= Calendar.getInstance();
		setDate(cal);
	}

	/** Returns a string containing the hebrew date in the form, "Month day, year" <BR>
	* e.g. "Teves 23, 5760" */
	public String getHebrewDateAsString()
	{
		return getHebrewMonthAsString() + " " + hebrewDate + ", " + hebrewYear;
	}
	/** Returns a string containing the Gregorian date in the form, "Month day, year" <BR>
	* e.g. "January 1, 2000".<P>
	* For more "standard" date processing, use getTime
	* and then the java.text.DateFormat class. */
	public String getDateAsString()
	{
		return getMonthAsString() + " " + date + ", " + year;
	}
	/** Returns a string containing the hebrew date in the form, "Month day, year"
	* e.g. "Teves 23, 5760" */
	public String toString()
	{
		return getHebrewDateAsString();
	}

	/** Rolls the date forward by 1. It modifies both the gregorian and hebrew dates
	*  accordingly */
	public void forward()
	{
	// Change gregorian date //
	if (date == getLastDayOfMonth())
	{
		// if last day of year //
		if (month== 12)
		{
			year++;
			month= 1;
			date= 1;
		}
		else
		{
			month++;
			date= 1;
		}
	}
	// if not last day of month //
	else
		date++;

	// Change Hebrew Date //
	if (hebrewDate== getLastDayOfHebrewMonth())
		{
		// if it last day of elul (i.e. last day of hebrew year)//
		if (hebrewMonth== 6)
		{
			hebrewYear++;
			hebrewMonth++;
			hebrewDate=1;
		}
		// if it is the last day of Adar, or Adar II as case may be //
		else if (hebrewMonth== getLastMonthOfHebrewYear())
		{
			hebrewMonth=1;
			hebrewDate=1;
		}
		else
			{
			hebrewMonth++;
			hebrewDate=1;
			}
		}
	// if not last date of month //
	else
		hebrewDate++;

	// if last day of week, loop back to sunday //
	if (day == 7)
		day= 1;
	else
		day++;

	// increment the absolute date //
	absDate++;
	}

	/** Rolls the date back by 1. It modifies both the gregorian and hebrew dates
	*  accordingly */
	public void back()
	{
	// Change gregorian date //
	// if first day of month //
	if (date== 1)
		{
		// if first day of year //
		if (month== 1)
			{
			month= 12;
			year--;
			}
		else
			month--;

		// change to last day of previous month //
		date= getLastDayOfMonth();
		}
	else
		date--;

	// change hebrew date //
	// if first day of hebrew month //
	if (hebrewDate== 1)
		{
		// if nissan //
		if (hebrewMonth== 1)
			hebrewMonth= getLastMonthOfHebrewYear();
		// if rosh hashana //
		else if (hebrewMonth== 7)
			{
			hebrewYear--;
			hebrewMonth--;
			}
		else
			hebrewMonth--;

		hebrewDate= getLastDayOfHebrewMonth();
		}
	else
		hebrewDate--;

	// if first day of week, loop back to saturday //
	if (day == 1)
		day= 7;
	else
		day--;

	// change the absolute date //
	absDate--;
	}


	/** Compares two dates to see if they are equal */
	public boolean equals (Object object)
	{
		HebrewDate hebDate= (HebrewDate) object;
		if (absDate != hebDate.getAbsDate())
			return false;
		else
			return true;
	}
	/** Compares two dates as per the compareTo() method in the Comparable interface.
	* Returns a value less than 0 if this date is "less than" (before) the date,
	* greater than 0 if this date is "greater than" (after) the date, or 0 if they are
	* equal. */
	public int compareTo(Object o)
	{
		HebrewDate hebDate= (HebrewDate) o;
		if (absDate < hebDate.getAbsDate())
			return -1;
		else if (absDate > hebDate.getAbsDate())
			return 1;
		else
			return 0;
	}


	/** returns a string  of the current hebrew month */
	public String getHebrewMonthAsString()
	{

	// if it is a leap year and 12th month //
	if (isHebrewLeapYear() && hebrewMonth==12)
		return "Adar I";

	else
		return hebrewMonths[hebrewMonth-1];
	}
	/** returns a string  of the current month */
	public String getMonthAsString()
	{
		return months[month-1];
	}
	/**
	* Returns the month (between 1-12).
	*/
	public int getMonth()
	{
		return month;
	}
	/**
	* Returns the date within the month.
	*/
	public int getDate()
	{
	return date;
	}
	/**
	* Returns the year.
	*/
	public int getYear()
	{
		return year;
	}
	/**
	* Returns the hebrew month (1-12 or 13).
	*/
	public int getHebrewMonth()
	{
		return hebrewMonth;
	}
	/**
	* Returns the hebrew date of the month.
	*/
	public int getHebrewDate()
	{
		return hebrewDate;
	}
	/**
	* Returns the hebrew year.
	*/
	public int getHebrewYear()
	{
		return hebrewYear;
	}
	/**
	* Returns the day of the week as a number between 1-7.
	*/
	public int getDayOfWeek()
	{
		return day;
	}
	/** sets the month. */
	public void setMonth(int month) throws HebrewDateException
	{
		setDate(month, CURRENT_DATE, CURRENT_YEAR);
	}
	/** sets the year. */
	public void setYear(int year) throws HebrewDateException
	{
		setDate(CURRENT_MONTH, CURRENT_DATE, year);
	}
	/** sets the date of the month. */
	public void setDate(int date) throws HebrewDateException
	{
		setDate(CURRENT_MONTH, date, CURRENT_YEAR);
	}
	/** sets the hebrew month. */
	public void setHebrewMonth(int month) throws HebrewDateException
	{
		setHebrewDate(month, CURRENT_DATE, CURRENT_YEAR);
	}
	/** sets the hebrew year. */
	public void setHebrewYear(int year) throws HebrewDateException
	{
		setDate(CURRENT_MONTH, 	CURRENT_DATE, year);
	}
	/** sets the hebrew date of month. */
	public void setHebrewDate(int date) throws HebrewDateException
	{
		setHebrewDate(CURRENT_MONTH, date, CURRENT_YEAR);
	}
	/** Create a copy of this date. */
	public Object clone()
	{
		try
		{
			return new HebrewDate(month, date, year);
		}
		catch (HebrewDateException e)
		{
			e.printStackTrace();
			return null;
		}
	}
}