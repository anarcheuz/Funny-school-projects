#include <set>
#include <utility>
#include <iostream>
#include <string>
#include <fstream>
#include <iomanip>

using namespace std;

int main(int argc, char** argv)
{
	set<pair<double,double> > points;
	ifstream entree;
	ofstream sortie;
	string ligneCourante;
	string nomFichier;
	if(argc>1) nomFichier=argv[1];
	else nomFichier="plateau.txt";
	entree.open(nomFichier.c_str());
	sortie.open("plateau.pl");
	char** carte;
	int largeurCarte,hauteurCarte;
	string points_list="free_lines([";
	hauteurCarte=0;
	while(getline(entree,ligneCourante))
	{
		largeurCarte=ligneCourante.length();
		hauteurCarte++;
	}
	entree.close();
	entree.open(nomFichier.c_str());
	carte=new char*[largeurCarte];
	for(int i=0;i<largeurCarte;i++)
	{
		carte[i]=new char[hauteurCarte];
	}
	for(int i=0;i<hauteurCarte;i++)
	{
		getline(entree,ligneCourante);
		for(int j=0;j<largeurCarte;j++)
		{
			carte[j][i]=ligneCourante.at(j);
		}
	}
	for(int i=0;i<largeurCarte;i++)
	{
		for(int j=0;j<hauteurCarte;j++)
		{
			if(carte[i][j]=='.')
			{
				points.insert(make_pair(i-0.5,j));
				points.insert(make_pair(i+0.5,j));
				points.insert(make_pair(i,j-0.5));
				points.insert(make_pair(i,j+0.5));

			}
		}
	}

	sortie<<fixed<<setprecision(1)<<"free_lines([["<<(points.begin())->first<<","<<(points.begin())->second<<"]";
	for(set<pair<double,double> >::iterator iCentre = ++points.begin();iCentre!=points.end();iCentre++)
	{
		sortie<<",["<<iCentre->first<<","<<iCentre->second<<"]";
	}
	sortie<<"])."<<endl;
	sortie<<"width_board("<<largeurCarte<<")."<<endl;
	sortie<<"height_board("<<hauteurCarte<<")."<<endl;
	entree.close();
	sortie.close();
	return 0;
}
