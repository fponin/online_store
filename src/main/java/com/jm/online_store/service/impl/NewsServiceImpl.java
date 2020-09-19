package com.jm.online_store.service.impl;

import com.jm.online_store.exception.NewsNotFoundException;
import com.jm.online_store.model.News;
import com.jm.online_store.model.dto.NewsFilterDto;
import com.jm.online_store.repository.NewsRepository;
import com.jm.online_store.service.interf.NewsService;
import com.jm.online_store.service.spec.NewsSpec;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Сервис класс, имплементация интерфейса {@link NewsService}
 * Содержит бизнес логику, использует методы репозитория {@link NewsRepository}
 */
@Service
@AllArgsConstructor
public class NewsServiceImpl implements NewsService {

    private final NewsRepository newsRepository;

    /**
     * Метод без параметров, который просто тащит список новостей
     *
     * @return List<News> возвращает список всех новостей
     */
    @Override
    public List<News> findAll() {
        List<News> allNews = newsRepository.findAll();
        if (allNews.isEmpty()) {
            throw new NewsNotFoundException("findAll returns empty List<News>");
        }
        return allNews;
    }

    /**
     * Метод извлекает страницу новостей
     *
     * @param page параметры страницы
     * @return Page<News> возвращает страницу новостей
     */
    @Override
    public Page<News> findAll(Pageable page, NewsFilterDto filterDto) {
        Specification<News> spec = NewsSpec.get(filterDto);
        Page<News> newsPage = newsRepository.findAll(spec, page);
        if (newsPage.isEmpty()) {
            throw new NewsNotFoundException();
        }
        return newsPage;
    }

    /**
     * Метод сохраняет сущность, пришедшую в качестве параметра
     *
     * @param news Сущность News c с заполненными полями
     */
    @Override
    public void save(News news) {
        newsRepository.save(news);
    }

    /**
     * Method accept Long id as parameter and returns {@link News} entity
     *
     * @param id - {@link Long}
     * @return returns News entity or throws {@link NewsNotFoundException}
     */
    @Override
    public News findById(long id) {
        return newsRepository.findById(id).orElseThrow(() -> new NewsNotFoundException("There are no news with such id"));
    }

    /**
     * Метод выполняет проверку существует ли сущность в базе.
     *
     * @param id уникальный идентификатор сушности
     * @return Возвращает булево значение true или false
     */
    @Override
    public boolean existsById(Long id) {
        return newsRepository.existsById(id);
    }

    /**
     * Метод обновляет сущность News в базе данных и изменяет modifiedDate на сегодняшнюю дату
     *
     * @param news сушность для обновления в базе данных
     */
    public News update(News news) {
        news.setModifiedDate(LocalDateTime.now());
        return newsRepository.save(news);
    }

    /**
     * Метод удаляет сущность News из базы данных по уникальному идентификатору
     *
     * @param id уникальный идентификатор сущности News
     */
    @Override
    public void deleteById(Long id) {
        newsRepository.deleteById(id);
    }

    /**
     * Метод делающий выборку из базы данных по заданному параметру,
     * где LocalDateTime postingDate > LocalDateTime timeNow.
     *
     * @return возвращает список еще неопубликованных новостей List<News>
     */
    @Override
    public List<News> getAllPublished() {
        List<News> publishedNews = newsRepository.findAllByPostingDateBeforeAndArchivedEquals(LocalDateTime.now(), false);
        if (publishedNews.isEmpty()) {
            throw new NewsNotFoundException("There are no published news");
        }
        return publishedNews;
    }

    /**
     * Метод делающий выборку из базы данных по заданному параметру,
     * где LocalDateTime postingDate <= LocalDateTime timeNow.
     *
     * @return возвращает список опубликованных новостей List<News>
     */
    @Override
    public List<News> getAllUnpublished() {
        List<News> unpublishedNews = newsRepository.findAllByPostingDateAfterAndArchivedEquals(LocalDateTime.now(), false);
        if (unpublishedNews.isEmpty()) {
            throw new NewsNotFoundException("There are no unpublished news");
        }
        return unpublishedNews;
    }

    /**
     * Method returns list of all archived news ot throw {@link NewsNotFoundException}
     * @return - List<News>
     */
    @Override
    public List<News> getAllArchivedNews() {
        List<News> archivedNews = newsRepository.findAllByArchivedEquals(true);
        if (archivedNews.isEmpty()) {
            throw new NewsNotFoundException("There are no archived news");
        }
        return archivedNews;
    }

}
