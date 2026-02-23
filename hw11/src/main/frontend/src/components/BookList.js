import React, { useState, useEffect } from 'react';
import AddBookForm from './AddBookForm';
import EditBookForm from './EditBookForm';
import { 
    Table, 
    TableBody, 
    TableCell, 
    TableContainer, 
    TableHead, 
    TableRow, 
    Paper,
    Typography,
    Container,
    Box,
    IconButton,
    Dialog,
    DialogTitle,
    DialogContent,
    DialogContentText,
    DialogActions,
    Button,
    Alert,
    Collapse
} from '@mui/material';
import EditIcon from '@mui/icons-material/Edit';
import DeleteIcon from '@mui/icons-material/Delete';
import * as api from '../services/api';

const BookList = () => {
    const [books, setBooks] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState('');
    const [editingBook, setEditingBook] = useState(null);
    const [authors, setAuthors] = useState([]);
    const [genres, setGenres] = useState([]);
    const [deleteConfirmation, setDeleteConfirmation] = useState({ open: false, book: null });
    const [actionSuccess, setActionSuccess] = useState('');

    useEffect(() => {
        loadData();
    }, []);

    const loadData = async () => {
        try {
            const [booksData, authorsData, genresData] = await Promise.all([
                api.fetchBooks(),
                api.fetchAuthors(),
                api.fetchGenres()
            ]);
            setBooks(booksData);
            setAuthors(authorsData);
            setGenres(genresData);
            setLoading(false);
        } catch (err) {
            setError(err.message);
            setLoading(false);
        }
    };

    const showSuccessMessage = (message) => {
        setActionSuccess(message);
        setTimeout(() => setActionSuccess(''), 3000);
    };

    const handleBookAdded = (newBook) => {
        setBooks(prevBooks => [...prevBooks, newBook]);
        showSuccessMessage('Книга успешно добавлена');
    };

    const handleEditClick = (book) => {
        setEditingBook(book);
    };

    const handleEditClose = () => {
        setEditingBook(null);
    };

    const handleBookUpdated = (updatedBook) => {
        setBooks(prevBooks => 
            prevBooks.map(book => 
                book.id === updatedBook.id ? updatedBook : book
            )
        );
        showSuccessMessage('Книга успешно обновлена');
    };

    const handleDeleteClick = (book) => {
        setDeleteConfirmation({ open: true, book });
    };

    const handleDeleteConfirm = async () => {
        const bookId = deleteConfirmation.book.id;
        try {
            await api.deleteBook(bookId);
            setBooks(prevBooks => prevBooks.filter(book => book.id !== bookId));
            setDeleteConfirmation({ open: false, book: null });
            showSuccessMessage('Книга успешно удалена');
        } catch (err) {
            setError(err.message);
            setDeleteConfirmation({ open: false, book: null });
        }
    };

    const handleDeleteCancel = () => {
        setDeleteConfirmation({ open: false, book: null });
    };

    if (loading) return (
        <Container maxWidth="md" sx={{ mt: 4 }}>
            <Typography>Загрузка...</Typography>
        </Container>
    );

    return (
        <Container maxWidth="md" sx={{ mt: 4 }}>
            <Typography variant="h4" component="h2" sx={{ my: 4, textAlign: 'center', color: 'primary.main' }}>
                Список книг
            </Typography>

            <Collapse in={Boolean(error)}>
                <Alert severity="error" onClose={() => setError('')} sx={{ mb: 2 }}>
                    {error}
                </Alert>
            </Collapse>

            <Collapse in={Boolean(actionSuccess)}>
                <Alert severity="success" sx={{ mb: 2 }}>
                    {actionSuccess}
                </Alert>
            </Collapse>

            <TableContainer component={Paper} elevation={3}>
                <Table>
                    <TableHead>
                        <TableRow>
                            <TableCell>Название</TableCell>
                            <TableCell>Автор</TableCell>
                            <TableCell>Жанр</TableCell>
                            <TableCell align="center">Действия</TableCell>
                        </TableRow>
                    </TableHead>
                    <TableBody>
                        {books.map(book => (
                            <TableRow key={book.id} hover>
                                <TableCell>{book.title}</TableCell>
                                <TableCell>{book.author.name}</TableCell>
                                <TableCell>{book.genre.name}</TableCell>
                                <TableCell align="center">
                                    <IconButton 
                                        onClick={() => handleEditClick(book)}
                                        color="primary"
                                        size="small"
                                    >
                                        <EditIcon />
                                    </IconButton>
                                    <IconButton 
                                        onClick={() => handleDeleteClick(book)}
                                        color="error"
                                        size="small"
                                    >
                                        <DeleteIcon />
                                    </IconButton>
                                </TableCell>
                            </TableRow>
                        ))}
                    </TableBody>
                </Table>
            </TableContainer>

            <Box sx={{ mt: 4 }}>
                <AddBookForm onBookAdded={handleBookAdded} />
            </Box>

            {editingBook && (
                <EditBookForm
                    book={editingBook}
                    authors={authors}
                    genres={genres}
                    open={Boolean(editingBook)}
                    onClose={handleEditClose}
                    onSave={handleBookUpdated}
                />
            )}

            <Dialog
                open={deleteConfirmation.open}
                onClose={handleDeleteCancel}
            >
                <DialogTitle>Подтверждение удаления</DialogTitle>
                <DialogContent>
                    <DialogContentText>
                        Вы действительно хотите удалить книгу "{deleteConfirmation.book?.title}"?
                    </DialogContentText>
                </DialogContent>
                <DialogActions>
                    <Button onClick={handleDeleteCancel}>Отмена</Button>
                    <Button onClick={handleDeleteConfirm} color="error" variant="contained">
                        Удалить
                    </Button>
                </DialogActions>
            </Dialog>
        </Container>
    );
};

export default BookList; 