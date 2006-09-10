#!/usr/bin/perl -wn

BEGIN
{
	$header = 0;
}

if ($_ =~ /^ *#/)
{
	# Comment line, do nothing
}
else
{
	my @fields = split(":");
	@fields = map { unpack("a*",$_) } @fields;
	if( !$header && $#fields == 2 )
	{
		print pack("c3", @fields);
		$header = 1;
	}
	elsif( $header && $#fields == 4 )
	{
		print pack("Cc4", @fields);
	}
	else
	{
		my $num = $header ? "5" : "3";
		die "Error, line had " . @fields . " fields, was expecting " .
			"$num.\n";
	}
}
