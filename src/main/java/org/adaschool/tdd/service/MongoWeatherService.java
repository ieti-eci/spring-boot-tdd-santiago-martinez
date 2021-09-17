package org.adaschool.tdd.service;

import org.adaschool.tdd.controller.weather.dto.WeatherReportDto;
import org.adaschool.tdd.repository.WeatherReportRepository;
import org.adaschool.tdd.repository.document.GeoLocation;
import org.adaschool.tdd.repository.document.WeatherReport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MongoWeatherService
    implements WeatherService
{

    private final WeatherReportRepository repository;

    public MongoWeatherService( @Autowired WeatherReportRepository repository )
    {
        this.repository = repository;
    }

    @Override
    public WeatherReport report( WeatherReportDto weatherReportDto )
    {
        WeatherReport report = new WeatherReport(weatherReportDto.getGeoLocation(), weatherReportDto.getTemperature(), weatherReportDto.getHumidity(), weatherReportDto.getReporter(), weatherReportDto.getCreated());
        repository.save(report);
        return report;
    }

    @Override
    public WeatherReport findById( String id )
    {
        return repository.findById(id).get();
    }

    @Override
    public List<WeatherReport> findNearLocation( GeoLocation geoLocation, float distanceRangeInMeters )
    {
        List<WeatherReport> all = repository.findAll();
        List<WeatherReport> nearLocationList = new ArrayList<>();
        System.out.println(all.size()+" lonfitud array");
        for(WeatherReport report: all){
            GeoLocation reportLocation = report.getGeoLocation();
            double inFormula = Math.pow((reportLocation.getLat()-geoLocation.getLat()),2) + Math.pow((reportLocation.getLng()-geoLocation.getLng()),2) ;
            System.out.println(inFormula);
            double distance = Math.sqrt(inFormula);
            System.out.println(distance);
            if(distance <= distanceRangeInMeters){
                nearLocationList.add(report);
            }
        }
        return nearLocationList;
    }

    @Override
    public List<WeatherReport> findWeatherReportsByName( String reporter )
    {
        List<WeatherReport> all = repository.findAll();
        List<WeatherReport> weatherNameList = new ArrayList<>();
        for(WeatherReport report: all){
            if(report.getReporter().equals(reporter)){
                weatherNameList.add(report);
            }
        }
        return weatherNameList;
    }
}
